package com.tuempresa.bodega.report;

import com.tuempresa.bodega.inventory.InventoryStock;
import com.tuempresa.bodega.inventory.InventoryStockRepository;
import com.tuempresa.bodega.movement.ingreso.IngresoHistorial;
import com.tuempresa.bodega.movement.ingreso.IngresoHistorialRepository;
import com.tuempresa.bodega.movement.salida.SalidaHistorial;
import com.tuempresa.bodega.movement.salida.SalidaHistorialRepository;
import com.tuempresa.bodega.product.Product;
import com.tuempresa.bodega.product.ProductRepository;
// --- NUEVOS IMPORTS PARA NOTAS ---
import com.tuempresa.bodega.note.Note;
import com.tuempresa.bodega.note.NoteRepository;
// ---------------------------------
import com.tuempresa.bodega.report.dto.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReporteService {

    private final IngresoHistorialRepository ingresoRepo;
    private final SalidaHistorialRepository salidaRepo;
    private final InventoryStockRepository stockRepo;
    private final ProductRepository productRepo;
    private final NoteRepository noteRepo; // <--- NUEVO: Acceso a Ventas Reales

    public ReporteService(IngresoHistorialRepository ingresoRepo, SalidaHistorialRepository salidaRepo, InventoryStockRepository stockRepo, ProductRepository productRepo, NoteRepository noteRepo) {
        this.ingresoRepo = ingresoRepo;
        this.salidaRepo = salidaRepo;
        this.stockRepo = stockRepo;
        this.productRepo = productRepo;
        this.noteRepo = noteRepo;
    }

    // =================================================================
    // 1. REPORTE GASTOS (Compras / Ingresos de Mercadería)
    // =================================================================
    public List<ReporteGastosDto> generarReporteGastos(ReporteRequestDto req) {
        List<IngresoHistorial> datos = ingresoRepo.findAll().stream()
                .filter(i -> filtrarIngreso(i, req))
                .collect(Collectors.toList());
        
        Map<String, List<IngresoHistorial>> agrupado = datos.stream().collect(Collectors.groupingBy(i -> {
            String k = obtenerAgrupadorIngreso(i, req.getEntidadFiltro()); return k==null?"Sin Info":k; 
        }));
        
        return agrupado.entrySet().stream().map(e -> {
            double dinero = e.getValue().stream().mapToDouble(IngresoHistorial::getTotalBruto).sum();
            double unids = e.getValue().stream().mapToDouble(IngresoHistorial::getCantidad).sum();
            long facs = e.getValue().stream().map(IngresoHistorial::getNumeroDocumento).filter(Objects::nonNull).distinct().count();
            return new ReporteGastosDto(e.getKey(), dinero, unids, facs);
        }).sorted((a,b) -> b.getTotalGastado().compareTo(a.getTotalGastado())).collect(Collectors.toList());
    }

    // =================================================================
    // 2. REPORTE CONSUMO (Salidas de Inventario: Consumo vs Merma)
    // =================================================================
    public List<ReporteConsumoDto> generarReporteConsumo(ReporteRequestDto req) {
        Map<String, Product> mapP = productRepo.findAll().stream().collect(Collectors.toMap(Product::getSku, p->p, (a,b)->a));
        
        // 1. Filtramos salidas (Fechas y filtros generales)
        List<SalidaHistorial> datos = salidaRepo.findAll().stream()
                .filter(s -> filtrarSalida(s, req, mapP))
                .collect(Collectors.toList());

        // 2. Preparamos FIFO (Ingresos ordenados)
        List<IngresoHistorial> todosIngresos = ingresoRepo.findAll();
        Map<String, List<IngresoHistorial>> ingresosPorSku = todosIngresos.stream()
                .sorted(Comparator.comparing(IngresoHistorial::getFecha))
                .collect(Collectors.groupingBy(IngresoHistorial::getProductSku));

        // 3. Agrupamos
        Map<String, List<SalidaHistorial>> agrupado = datos.stream().collect(Collectors.groupingBy(s -> {
            String k = obtenerAgrupadorSalida(s, req.getEntidadFiltro(), mapP); 
            return (k == null || k.trim().isEmpty()) ? "Sin Info" : k; 
        }));

        return agrupado.entrySet().stream().map(e -> {
            
            // 4. Filtro Estricto de Área (Evita sumar duplicados si hay filtro global)
            List<SalidaHistorial> listaFiltrada = e.getValue().stream()
                .filter(s -> cumpleFiltroAreaEstricto(s, req)) 
                .collect(Collectors.toList());

            long guias = listaFiltrada.size();
            
            // Variables para desglose
            double sumCantConsumo = 0;
            double sumValConsumo = 0;
            double sumCantMerma = 0;
            double sumValMerma = 0;

            // Agrupar por SKU para FIFO preciso
            Map<String, List<SalidaHistorial>> porSku = listaFiltrada.stream()
                .collect(Collectors.groupingBy(SalidaHistorial::getProductSku));
            
            for(Map.Entry<String, List<SalidaHistorial>> entradaSku : porSku.entrySet()) {
                String sku = entradaSku.getKey();
                List<SalidaHistorial> salidasSku = entradaSku.getValue();
                
                // Separar Merma de Consumo
                double cantC = salidasSku.stream().filter(s -> !"MERMA".equalsIgnoreCase(s.getTipoSalida())).mapToDouble(SalidaHistorial::getCantidad).sum();
                double cantM = salidasSku.stream().filter(s -> "MERMA".equalsIgnoreCase(s.getTipoSalida())).mapToDouble(SalidaHistorial::getCantidad).sum();
                
                // Calcular Costo FIFO del lote total
                List<IngresoHistorial> compras = ingresosPorSku.getOrDefault(sku, new ArrayList<>());
                double costoTotalLote = calcularCostoFIFO(cantC + cantM, compras);
                double precioUnitarioPromedio = (cantC + cantM > 0) ? costoTotalLote / (cantC + cantM) : 0;

                sumCantConsumo += cantC;
                sumValConsumo += (cantC * precioUnitarioPromedio);
                sumCantMerma += cantM;
                sumValMerma += (cantM * precioUnitarioPromedio);
            }
            
            double unidsTotal = sumCantConsumo + sumCantMerma;
            double valorTotal = sumValConsumo + sumValMerma;

            return new ReporteConsumoDto(e.getKey(), unidsTotal, guias, valorTotal, sumCantConsumo, sumValConsumo, sumCantMerma, sumValMerma);

        }).sorted((a,b) -> b.getTotalUnidades().compareTo(a.getTotalUnidades())).collect(Collectors.toList());
    }

    // =================================================================
    // 3. REPORTE STOCK FINAL (Inventario Físico Valorizado)
    // =================================================================
    public List<ReporteStockDto> generarReporteStock(ReporteRequestDto req) {
        List<InventoryStock> datos = stockRepo.findAll().stream()
                .filter(s -> filtrarStock(s, req))
                .collect(Collectors.toList());
        
        List<IngresoHistorial> historial = ingresoRepo.findAll();
        Map<String, Double> mapaPreciosPromedio = historial.stream()
                .collect(Collectors.groupingBy(
                    IngresoHistorial::getProductSku,
                    Collectors.collectingAndThen(Collectors.toList(), lista -> {
                            double totalGastado = lista.stream().mapToDouble(IngresoHistorial::getTotalBruto).sum();
                            double totalComprado = lista.stream().mapToDouble(IngresoHistorial::getCantidad).sum();
                            return (totalComprado > 0) ? (totalGastado / totalComprado) : 0.0;
                        })));

        Map<String, List<InventoryStock>> agrupado = datos.stream().collect(Collectors.groupingBy(s -> {
            String k = obtenerAgrupadorStock(s, req.getEntidadFiltro()); return k==null?"Sin Info":k; 
        }));

        return agrupado.entrySet().stream().map(e -> {
            double totalStock = e.getValue().stream().mapToDouble(InventoryStock::getCantidad).sum();
            long numLotes = e.getValue().size();
            long itemsUnicos = e.getValue().stream().map(s -> s.getProduct().getSku()).distinct().count();
            double valorTotal = e.getValue().stream().mapToDouble(s -> {
                Double ppp = mapaPreciosPromedio.getOrDefault(s.getProduct().getSku(), 0.0);
                return s.getCantidad() * ppp;
            }).sum();
            return new ReporteStockDto(e.getKey(), totalStock, numLotes, itemsUnicos, valorTotal);
        }).sorted((a, b) -> b.getValorTotal().compareTo(a.getValorTotal())).collect(Collectors.toList());
    }

    // =================================================================
    // 4. COMPARATIVO (Ingresos vs Salidas)
    // =================================================================
    public List<ReporteChartDto> generarReporteComparativo(ReporteRequestDto req) {
        // A. Ingresos
        List<IngresoHistorial> ing = ingresoRepo.findAll().stream().filter(i -> filtrarIngreso(i, req)).toList();
        Map<String, List<IngresoHistorial>> mapIngresos = ing.stream().collect(Collectors.groupingBy(i -> obtenerAgrupadorIngreso(i, req.getEntidadFiltro())));

        // B. Salidas
        Map<String, Product> mapP = productRepo.findAll().stream().collect(Collectors.toMap(Product::getSku, p->p, (a,b)->a));
        List<SalidaHistorial> sal = salidaRepo.findAll().stream().filter(s -> filtrarSalida(s, req, mapP)).toList();
        
        List<IngresoHistorial> todosIngresos = ingresoRepo.findAll();
        todosIngresos.sort(Comparator.comparing(IngresoHistorial::getFecha));
        Map<String, List<IngresoHistorial>> ingresosPorSku = todosIngresos.stream().collect(Collectors.groupingBy(IngresoHistorial::getProductSku));

        Map<String, List<SalidaHistorial>> mapSalidas = sal.stream().collect(Collectors.groupingBy(s -> {
            String k=obtenerAgrupadorSalida(s, req.getEntidadFiltro(), mapP); return k==null?"-":k;
        }));

        // C. Fusión
        Set<String> keys = new HashSet<>(); keys.addAll(mapIngresos.keySet()); keys.addAll(mapSalidas.keySet());
        
        return keys.stream().map(key -> {
            // Datos Ingreso
            List<IngresoHistorial> listaIng = mapIngresos.getOrDefault(key, new ArrayList<>());
            double inDinero = listaIng.stream().mapToDouble(IngresoHistorial::getTotalBruto).sum();
            double inCant = listaIng.stream().mapToDouble(IngresoHistorial::getCantidad).sum();

            // Datos Salida (Con filtro estricto)
            List<SalidaHistorial> listaSal = mapSalidas.getOrDefault(key, new ArrayList<>());
            List<SalidaHistorial> listaSalFiltrada = listaSal.stream()
                .filter(s -> cumpleFiltroAreaEstricto(s, req))
                .collect(Collectors.toList());

            double outCant = listaSalFiltrada.stream().mapToDouble(SalidaHistorial::getCantidad).sum();
            double outDinero = 0;
            Map<String, Double> porSku = listaSalFiltrada.stream().collect(Collectors.groupingBy(SalidaHistorial::getProductSku, Collectors.summingDouble(SalidaHistorial::getCantidad)));
            for(Map.Entry<String, Double> item : porSku.entrySet()) {
                outDinero += calcularCostoFIFO(item.getValue(), ingresosPorSku.getOrDefault(item.getKey(), new ArrayList<>()));
            }
            return new ReporteChartDto(key, inDinero, inCant, outDinero, outCant);
        }).sorted((a,b) -> Double.compare(b.getIngresoDinero(), a.getIngresoDinero())).collect(Collectors.toList());
    }

    // =================================================================
    // 5. NUEVO: REPORTE VENTA DIARIA (Fuente: Tabla de NOTAS / CAJA)
    // =================================================================
    public List<ReporteVentaDiariaDto> generarReporteVentaDiaria(ReporteRequestDto req) {
        
        // 1. Obtener todas las notas
        List<Note> todasLasNotas = noteRepo.findAll();

        // 2. Filtrar por Fecha y Categoría "Venta"
        List<Note> notasFiltradas = todasLasNotas.stream()
            .filter(n -> !n.getDate().isBefore(req.getFechaInicio()) && !n.getDate().isAfter(req.getFechaFin()))
            .filter(n -> {
                if (n.getCategory() == null) return false;
                String cat = n.getCategory().toLowerCase();
                // Ajusta esto a como guardas tus notas: "venta", "caja", etc.
                return cat.contains("venta"); 
            })
            .collect(Collectors.toList());

        // 3. Agrupar por FECHA
        Map<LocalDate, List<Note>> agrupadoPorFecha = notasFiltradas.stream()
                .collect(Collectors.groupingBy(Note::getDate));

        // 4. Calcular Totales
        List<ReporteVentaDiariaDto> resultado = new ArrayList<>();

        for (Map.Entry<LocalDate, List<Note>> entry : agrupadoPorFecha.entrySet()) {
            LocalDate fecha = entry.getKey();
            List<Note> notasDelDia = entry.getValue();

            // Cantidad = Número de notas
            double cantidadNotas = notasDelDia.size();
            
            // Valor = Suma del monto ($)
            double montoTotal = notasDelDia.stream()
                .mapToDouble(n -> (n.getAmount() != null) ? n.getAmount() : 0.0)
                .sum();

            resultado.add(new ReporteVentaDiariaDto(fecha, cantidadNotas, montoTotal));
        }

        // 5. Ordenar cronológicamente
        resultado.sort(Comparator.comparing(ReporteVentaDiariaDto::getFecha));
        return resultado;
    }

    // --- HELPERS Y LÓGICA DE NEGOCIO ---

    private boolean cumpleFiltroAreaEstricto(SalidaHistorial s, ReporteRequestDto req) {
        String areaRealDeEstaFila = s.getAreaOrigen();
        if (s.getAreaDestino() != null && !s.getAreaDestino().equalsIgnoreCase("Consumo Interno") && !s.getAreaDestino().equalsIgnoreCase(s.getAreaOrigen())) {
            areaRealDeEstaFila = s.getAreaDestino();
        }
        if (req.getFiltroGlobalArea() != null && !req.getFiltroGlobalArea().isEmpty()) {
            if (!coincideFlexible(Collections.singletonList(req.getFiltroGlobalArea()), areaRealDeEstaFila)) return false;
        }
        if ("AREA".equals(req.getEntidadFiltro()) && req.getValoresFiltro() != null && !req.getValoresFiltro().isEmpty()) {
            return coincideFlexible(req.getValoresFiltro(), areaRealDeEstaFila);
        }
        return true;
    }

    private boolean coincideFlexible(List<String> filtros, String valorBD) {
        if (valorBD == null) return false;
        return filtros.stream().anyMatch(f -> f.trim().equalsIgnoreCase(valorBD.trim()));
    }

    private double calcularCostoFIFO(double cant, List<IngresoHistorial> ings) {
        double costo=0; double pend=cant;
        for(IngresoHistorial i : ings) {
            if(pend<=0) break;
            double pUnit = (i.getCantidad()>0)?i.getTotalBruto()/i.getCantidad():0;
            double tomar = Math.min(i.getCantidad(), pend);
            costo += tomar*pUnit; pend -= tomar;
        }
        return costo;
    }

    private String obtenerAgrupadorIngreso(IngresoHistorial i, String e) { 
        String k=""; if(e==null)e="CATEGORIA"; 
        switch(e){case "PROVEEDOR":k=i.getSupplierName();break;case "CATEGORIA":k=i.getCategory();break;case "AREA":k=i.getAreaNombre();break;case "PRODUCTO":k=i.getProductName();break;default:k=i.getCategory();} return (k!=null&&!k.trim().isEmpty())?k:"Sin Info"; 
    }

    private String obtenerAgrupadorSalida(SalidaHistorial s, String e, Map<String, Product> m) { 
        String k=""; if(e==null)e="PRODUCTO"; Product p=m.get(s.getProductSku()); 
        switch(e){
            case "PROVEEDOR":k=(p!=null)?p.getSupplierName():"";break;
            case "CATEGORIA":k=(p!=null)?p.getCategory():"";break;
            case "PRODUCTO":k=s.getProductName();break;
            case "AREA":
                if (s.getAreaDestino() != null && !s.getAreaDestino().equalsIgnoreCase("Consumo Interno") && !s.getAreaDestino().equalsIgnoreCase(s.getAreaOrigen())) k = s.getAreaDestino(); else k = s.getAreaOrigen();
                break;
            default:k=s.getProductName();
        } 
        return (k!=null&&!k.trim().isEmpty())?k:"Sin Info"; 
    }

    private String obtenerAgrupadorStock(InventoryStock s, String e) { 
        String k=""; if(e==null)e="CATEGORIA"; 
        switch(e){case "PROVEEDOR":k=s.getProduct().getSupplierName();break;case "CATEGORIA":k=s.getProduct().getCategory();break;case "AREA":k=s.getAreaDeTrabajo().getNombre();break;default:k=s.getProduct().getName();} return (k!=null&&!k.trim().isEmpty())?k:"Sin Info"; 
    }
    
    private boolean filtrarSalida(SalidaHistorial s, ReporteRequestDto req, Map<String, Product> m) { 
        if(s.getFecha().isBefore(req.getFechaInicio()) || s.getFecha().isAfter(req.getFechaFin())) return false; 
        
        // Filtro Global Área
        if (req.getFiltroGlobalArea() != null && !req.getFiltroGlobalArea().isEmpty()) {
            String areaReal = s.getAreaOrigen();
            if (s.getAreaDestino() != null && !s.getAreaDestino().equalsIgnoreCase("Consumo Interno")) areaReal = s.getAreaDestino();
            if (!coincideFlexible(Collections.singletonList(req.getFiltroGlobalArea()), areaReal)) return false;
        }
        
        // Filtro Tipo Salida (Aunque ahora mostramos todo en columnas, por si acaso)
        if (req.getFiltroTipoSalida() != null && !req.getFiltroTipoSalida().equals("TODOS")) {
            String tipoBD = (s.getTipoSalida() != null) ? s.getTipoSalida() : "CONSUMO";
            if (!tipoBD.equalsIgnoreCase(req.getFiltroTipoSalida())) return false;
        }

        if(req.getValoresFiltro() == null || req.getValoresFiltro().isEmpty()) return true; 
        
        Product p = m.get(s.getProductSku()); 
        switch(req.getEntidadFiltro()) { 
            case "AREA": return true; 
            case "PRODUCTO": String skuActual = s.getProductSku(); return req.getValoresFiltro().stream().anyMatch(f -> f.startsWith(skuActual + " - ") || f.equals(skuActual));
            case "CATEGORIA": return coincideFlexible(req.getValoresFiltro(), p != null ? p.getCategory() : ""); 
            case "PROVEEDOR": return coincideFlexible(req.getValoresFiltro(), p != null ? p.getSupplierName() : ""); 
            default: return true; 
        } 
    }

    private boolean filtrarIngreso(IngresoHistorial i, ReporteRequestDto req) { 
        if(i.getFecha().isBefore(req.getFechaInicio())||i.getFecha().isAfter(req.getFechaFin()))return false; 
        if(req.getValoresFiltro()==null||req.getValoresFiltro().isEmpty())return true; 
        switch(req.getEntidadFiltro()){ 
            case "PRODUCTO": String skuActual = i.getProductSku(); return req.getValoresFiltro().stream().anyMatch(f -> f.startsWith(skuActual + " - ") || f.equals(skuActual));
            case "PROVEEDOR": return coincideFlexible(req.getValoresFiltro(), i.getSupplierName());
            case "CATEGORIA": return coincideFlexible(req.getValoresFiltro(), i.getCategory());
            case "AREA": return coincideFlexible(req.getValoresFiltro(), i.getAreaNombre());
            default:return true; 
        } 
    }

    private boolean filtrarStock(InventoryStock s, ReporteRequestDto req) { 
        if (req.getFiltroGlobalArea() != null && !req.getFiltroGlobalArea().isEmpty()) {
            if (!coincideFlexible(Collections.singletonList(req.getFiltroGlobalArea()), s.getAreaDeTrabajo().getNombre())) return false;
        }
        if(req.getValoresFiltro()==null||req.getValoresFiltro().isEmpty())return true; 
        switch(req.getEntidadFiltro()){ 
            case "PRODUCTO": String skuActual = s.getProduct().getSku(); return req.getValoresFiltro().stream().anyMatch(f -> f.startsWith(skuActual + " - ") || f.equals(skuActual));
            case "PROVEEDOR": return coincideFlexible(req.getValoresFiltro(), s.getProduct().getSupplierName());
            case "CATEGORIA": return coincideFlexible(req.getValoresFiltro(), s.getProduct().getCategory());
            case "AREA": return coincideFlexible(req.getValoresFiltro(), s.getAreaDeTrabajo().getNombre());
            default:return true; 
        } 
    }
}