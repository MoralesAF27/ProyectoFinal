
package asistenciamotor360.modelo;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;

import asistenciamotor360.modelo.estructuras.Cola;
import asistenciamotor360.modelo.estructuras.ColaPrioridad;
import asistenciamotor360.modelo.estructuras.Comparador;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.modelo.estructuras.Pila;
import asistenciamotor360.modelo.estructuras.TablaHash;

public class SistemaAsistencia {

    private static final int UMBRAL_PRIORIDAD = 18;
    private static final int PUNTAJE_MAXIMO = 50;
    private static final int PESO_RIESGO = 3;
    private static final int PESO_AFECTACION = 2;
    private static final int PESO_SEVERIDAD = 2;
    private static final int MAX_HISTORIAL = 120;
    private static final int MAXIMO_TRANSACCIONES = 60;

    private final TablaHash<String, UnidadMovil> unidades;
    private final TablaHash<String, Tecnico> tecnicos;
    private final TablaHash<String, Cliente> clientes;
    private final TablaHash<String, Solicitud> solicitudes;
    private final Cola<Solicitud> colaOrdinaria;
    private final ColaPrioridad<Solicitud> colaPrioritaria;
    private final Lista<Solicitud> serviciosActivos;
    private final Lista<Solicitud> serviciosSolucionados;
    private final Pila<Maletin> maletinesDisponibles;
    private final Pila<Maletin> maletinesAuditoria;
    private final Lista<Maletin> maletinesEnServicio;
    private final Lista<Maletin> maletinesDescartados;
    private final Pila<Repuesto> repuestosDisponibles;
    private final Lista<Repuesto> repuestosDespachados;
    private final Lista<RegistroAccion> historial;
    private final Pila<Transaccion> transacciones;
    private int consecutivoSolicitud;
    private int consecutivoTransaccion;

    public SistemaAsistencia() {
        this.unidades = new TablaHash<String, UnidadMovil>();
        this.tecnicos = new TablaHash<String, Tecnico>();
        this.clientes = new TablaHash<String, Cliente>();
        this.solicitudes = new TablaHash<String, Solicitud>();
        this.colaOrdinaria = new Cola<Solicitud>();
        this.colaPrioritaria = new ColaPrioridad<Solicitud>(new Comparador<Solicitud>() {
            @Override
            public int comparar(Solicitud primero, Solicitud segundo) {
                if (primero.getPuntajePrioridad() != segundo.getPuntajePrioridad()) {
                    return primero.getPuntajePrioridad() - segundo.getPuntajePrioridad();
                }
                return segundo.getFechaRecepcion().compareTo(primero.getFechaRecepcion());
            }
        });
        this.serviciosActivos = new Lista<Solicitud>();
        this.serviciosSolucionados = new Lista<Solicitud>();
        this.maletinesDisponibles = new Pila<Maletin>();
        this.maletinesAuditoria = new Pila<Maletin>();
        this.maletinesEnServicio = new Lista<Maletin>();
        this.maletinesDescartados = new Lista<Maletin>();
        this.repuestosDisponibles = new Pila<Repuesto>();
        this.repuestosDespachados = new Lista<Repuesto>();
        this.historial = new Lista<RegistroAccion>();
        this.transacciones = new Pila<Transaccion>();
        this.consecutivoSolicitud = 0;
        this.consecutivoTransaccion = 0;
    }

    public UnidadMovil registrarUnidad(String codigo, TipoUnidad tipo, ZonaOperacion zona,
            NivelDisponibilidad disponibilidad) {
        validarTexto(codigo, "El codigo de la unidad es obligatorio.");
        if (tipo == null || zona == null || disponibilidad == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar tipo, zona y disponibilidad.");
        }
        if (buscarUnidadPorCodigo(codigo) != null) {
            throw new ReglaNegocioExcepcion("Ya existe una unidad con el codigo " + codigo + ".");
        }
        String uuid = UUID.randomUUID().toString();
        UnidadMovil unidad = new UnidadMovil(uuid, codigo.trim(), tipo, EstadoUnidad.DISPONIBLE,
                zona, disponibilidad);
        unidades.poner(uuid, unidad);
        registrarAccion("FLOTA", "Unidad " + unidad.getCodigo() + " registrada con UUID " + uuid);
        registrarTransaccion("Registro de unidad " + unidad.getCodigo(), new Runnable() {
            @Override
            public void run() {
                unidades.eliminar(uuid);
            }
        });
        return unidad;
    }

    public void cambiarEstadoUnidad(String uuid, EstadoUnidad nuevoEstado) {
        UnidadMovil unidad = obtenerUnidad(uuid);
        if (nuevoEstado == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar el nuevo estado de la unidad.");
        }
        if (nuevoEstado == EstadoUnidad.ASIGNADA) {
            throw new ReglaNegocioExcepcion(
                    "El estado asignada solo se obtiene mediante un despacho de servicio.");
        }
        if (tieneServicioActivo(unidad)) {
            throw new ReglaNegocioExcepcion(
                    "La unidad " + unidad.getCodigo() + " tiene un servicio activo. "
                            + "Solucione el servicio antes de cambiar su estado.");
        }
        EstadoUnidad anterior = unidad.getEstado();
        unidad.setEstado(nuevoEstado);
        registrarAccion("FLOTA", "Unidad " + unidad.getCodigo() + " cambio de "
                + anterior.getEtiqueta() + " a " + nuevoEstado.getEtiqueta());
        registrarTransaccion("Cambio de estado de la unidad " + unidad.getCodigo(),
                new Runnable() {
                    @Override
                    public void run() {
                        unidad.setEstado(anterior);
                    }
                });
    }

    public Tecnico registrarTecnico(String identificacion, String nombre,
            Especialidad especialidad, ZonaOperacion sector) {
        validarTexto(identificacion, "La identificacion del tecnico es obligatoria.");
        validarTexto(nombre, "El nombre del tecnico es obligatorio.");
        if (especialidad == null || sector == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar especialidad y sector.");
        }
        if (tecnicos.contieneClave(identificacion.trim())) {
            throw new ReglaNegocioExcepcion(
                    "Ya existe un tecnico con la identificacion " + identificacion + ".");
        }
        Tecnico tecnico = new Tecnico(identificacion.trim(), nombre.trim(), especialidad,
                EstadoTecnico.DISPONIBLE, sector);
        tecnicos.poner(tecnico.getIdentificacion(), tecnico);
        registrarAccion("PERSONAL", "Tecnico " + tecnico.getNombre() + " registrado en "
                + sector.getEtiqueta());
        registrarTransaccion("Registro del tecnico " + tecnico.getNombre(), new Runnable() {
            @Override
            public void run() {
                tecnicos.eliminar(tecnico.getIdentificacion());
            }
        });
        return tecnico;
    }

    public void cambiarEstadoTecnico(String identificacion, EstadoTecnico nuevoEstado) {
        Tecnico tecnico = obtenerTecnico(identificacion);
        if (nuevoEstado == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar el nuevo estado del tecnico.");
        }
        if (nuevoEstado == EstadoTecnico.EN_ATENCION) {
            throw new ReglaNegocioExcepcion(
                    "El estado en atencion solo se obtiene mediante un despacho de servicio.");
        }
        if (tecnico.getIdServicioActivo() != null) {
            throw new ReglaNegocioExcepcion("El tecnico " + tecnico.getNombre()
                    + " tiene un servicio activo y no puede cambiar de estado.");
        }
        EstadoTecnico anterior = tecnico.getEstado();
        tecnico.setEstado(nuevoEstado);
        registrarAccion("PERSONAL", "Tecnico " + tecnico.getNombre() + " cambio de "
                + anterior.getEtiqueta() + " a " + nuevoEstado.getEtiqueta());
        registrarTransaccion("Cambio de estado del tecnico " + tecnico.getNombre(),
                new Runnable() {
                    @Override
                    public void run() {
                        tecnico.setEstado(anterior);
                    }
                });
    }

    public Cliente registrarCliente(String identificacion, String nombre, TipoCliente tipo,
            String telefono, String direccion) {
        validarTexto(identificacion, "La identificacion del cliente es obligatoria.");
        validarTexto(nombre, "El nombre del cliente es obligatorio.");
        if (tipo == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar el tipo de cliente.");
        }
        if (clientes.contieneClave(identificacion.trim())) {
            throw new ReglaNegocioExcepcion(
                    "Ya existe un cliente con la identificacion " + identificacion + ".");
        }
        Cliente cliente = new Cliente(identificacion.trim(), nombre.trim(), tipo,
                telefono == null ? "" : telefono.trim(), direccion == null ? "" : direccion.trim());
        clientes.poner(cliente.getIdentificacion(), cliente);
        registrarAccion("CLIENTES", "Cliente " + cliente.getNombre() + " registrado como "
                + tipo.getEtiqueta());
        registrarTransaccion("Registro del cliente " + cliente.getNombre(), new Runnable() {
            @Override
            public void run() {
                clientes.eliminar(cliente.getIdentificacion());
            }
        });
        return cliente;
    }

    public int calcularPuntaje(int nivelRiesgo, int afectacionVial, int severidad,
            TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            return 0;
        }
        int puntaje = nivelRiesgo * PESO_RIESGO + afectacionVial * PESO_AFECTACION
                + severidad * PESO_SEVERIDAD + tipoCliente.getPesoPrioridad();
        return Math.min(PUNTAJE_MAXIMO, puntaje);
    }

    public boolean requiereAtencionPrioritaria(int puntaje, TipoCliente tipoCliente) {
        return puntaje >= UMBRAL_PRIORIDAD || TipoCliente.SERVICIO_PUBLICO.equals(tipoCliente);
    }

    public String describirPrioridad(int nivelRiesgo, int afectacionVial, int severidad,
            TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            return "Seleccione un cliente para evaluar la prioridad.";
        }
        int puntaje = calcularPuntaje(nivelRiesgo, afectacionVial, severidad, tipoCliente);
        String clasificacion = requiereAtencionPrioritaria(puntaje, tipoCliente)
                ? "PRIORITARIA" : "ORDINARIA";
        return "Riesgo " + nivelRiesgo + " x" + PESO_RIESGO + " + afectacion vial "
                + afectacionVial + " x" + PESO_AFECTACION + " + severidad " + severidad + " x"
                + PESO_SEVERIDAD + " + perfil " + tipoCliente.getEtiqueta() + " +"
                + tipoCliente.getPesoPrioridad() + " = " + puntaje + "/" + PUNTAJE_MAXIMO
                + " -> " + clasificacion;
    }

    public Solicitud crearSolicitud(String identificacionCliente, TipoIncidente tipoIncidente,
            ZonaOperacion zona, int nivelRiesgo, int afectacionVial, int severidad,
            String descripcion) {
        Cliente cliente = clientes.obtener(identificacionCliente);
        if (cliente == null) {
            throw new ReglaNegocioExcepcion(
                    "Toda solicitud debe estar vinculada a un cliente registrado.");
        }
        if (tipoIncidente == null || zona == null) {
            throw new ReglaNegocioExcepcion("Debe seleccionar el tipo de incidente y la zona.");
        }
        validarRango(nivelRiesgo, "El nivel de riesgo");
        validarRango(afectacionVial, "La afectacion vial");
        validarRango(severidad, "La severidad");
        validarTexto(descripcion, "La descripcion del incidente es obligatoria.");
        consecutivoSolicitud++;
        String id = String.format("SOL-%04d", consecutivoSolicitud);
        Solicitud solicitud = new Solicitud(id, cliente, tipoIncidente, descripcion.trim(), zona,
                nivelRiesgo, afectacionVial, severidad);
        int puntaje = calcularPuntaje(nivelRiesgo, afectacionVial, severidad, cliente.getTipo());
        solicitud.setPuntajePrioridad(puntaje);
        solicitud.setPrioritaria(requiereAtencionPrioritaria(puntaje, cliente.getTipo()));
        solicitud.setMotivoPrioridad(describirPrioridad(nivelRiesgo, afectacionVial, severidad,
                cliente.getTipo()));
        solicitudes.poner(id, solicitud);
        if (solicitud.isPrioritaria()) {
            colaPrioritaria.encolar(solicitud);
        } else {
            colaOrdinaria.encolar(solicitud);
        }
        registrarAccion("SOLICITUDES", "Solicitud " + id + " de " + cliente.getNombre()
                + " registrada como " + (solicitud.isPrioritaria() ? "prioritaria" : "ordinaria")
                + " con puntaje " + puntaje);
        registrarTransaccion("Registro de la solicitud " + id, new Runnable() {
            @Override
            public void run() {
                if (solicitud.isPrioritaria()) {
                    colaPrioritaria.eliminar(solicitud);
                } else {
                    colaOrdinaria.eliminar(solicitud);
                }
                solicitudes.eliminar(solicitud.getId());
            }
        });
        return solicitud;
    }

    public void escalarSolicitud(String idSolicitud) {
        Solicitud solicitud = obtenerSolicitud(idSolicitud);
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ReglaNegocioExcepcion(
                    "Solo las solicitudes pendientes pueden escalarse.");
        }
        if (solicitud.isPrioritaria()) {
            throw new ReglaNegocioExcepcion("La solicitud " + idSolicitud + " ya es prioritaria.");
        }
        Lista<Solicitud> fotoOrdinaria = colaOrdinaria.listar();
        Lista<Solicitud> fotoPrioritaria = colaPrioritaria.listar();
        boolean anteriorPrioritaria = solicitud.isPrioritaria();
        int puntajeAnterior = solicitud.getPuntajePrioridad();
        String motivoAnterior = solicitud.getMotivoPrioridad();
        colaOrdinaria.eliminar(solicitud);
        solicitud.setPuntajePrioridad(Math.min(PUNTAJE_MAXIMO, puntajeAnterior + 10));
        solicitud.setPrioritaria(true);
        solicitud.setMotivoPrioridad(motivoAnterior
                + " | Escalada manualmente por el supervisor");
        colaPrioritaria.encolar(solicitud);
        registrarAccion("SOLICITUDES", "Solicitud " + idSolicitud
                + " escalada a atencion prioritaria por el supervisor");
        registrarTransaccion("Escalamiento de la solicitud " + idSolicitud, new Runnable() {
            @Override
            public void run() {
                solicitud.setPrioritaria(anteriorPrioritaria);
                solicitud.setPuntajePrioridad(puntajeAnterior);
                solicitud.setMotivoPrioridad(motivoAnterior);
                colaOrdinaria.cargar(fotoOrdinaria);
                colaPrioritaria.cargar(fotoPrioritaria);
            }
        });
    }

    public Solicitud solicitudSiguiente() {
        if (!colaPrioritaria.estaVacia()) {
            return colaPrioritaria.mirar();
        }
        if (!colaOrdinaria.estaVacia()) {
            return colaOrdinaria.frente();
        }
        return null;
    }

    public Solicitud despachar(String idSolicitud, String uuidUnidad, String idTecnico) {
        Solicitud solicitud = obtenerSolicitud(idSolicitud);
        if (solicitud.getEstado() != EstadoSolicitud.PENDIENTE) {
            throw new ReglaNegocioExcepcion(
                    "La solicitud " + idSolicitud + " ya no se encuentra pendiente.");
        }
        Solicitud siguiente = solicitudSiguiente();
        if (siguiente == null || !siguiente.getId().equals(solicitud.getId())) {
            throw new ReglaNegocioExcepcion("Debe atenderse primero la solicitud "
                    + (siguiente == null ? "pendiente" : siguiente.getId())
                    + " para respetar el orden de atencion.");
        }
        UnidadMovil unidad = obtenerUnidad(uuidUnidad);
        if (unidad.getEstado() == EstadoUnidad.MANTENIMIENTO) {
            throw new ReglaNegocioExcepcion("La unidad " + unidad.getCodigo()
                    + " esta en mantenimiento y no puede asignarse a un caso.");
        }
        if (unidad.getEstado() == EstadoUnidad.ASIGNADA) {
            throw new ReglaNegocioExcepcion("La unidad " + unidad.getCodigo()
                    + " ya esta asignada y no puede figurar como disponible.");
        }
        if (unidad.getEstado() != EstadoUnidad.DISPONIBLE) {
            throw new ReglaNegocioExcepcion("La unidad " + unidad.getCodigo()
                    + " no esta disponible para despacho. Estado actual: "
                    + unidad.getEstado().getEtiqueta() + ".");
        }
        Tecnico tecnico = obtenerTecnico(idTecnico);
        if (!tecnico.estaDisponible()) {
            throw new ReglaNegocioExcepcion("El tecnico " + tecnico.getNombre()
                    + " ya tiene un servicio activo o no esta disponible.");
        }

        Lista<Solicitud> fotoOrdinaria = colaOrdinaria.listar();
        Lista<Solicitud> fotoPrioritaria = colaPrioritaria.listar();
        EstadoUnidad estadoUnidadAnterior = unidad.getEstado();
        EstadoTecnico estadoTecnicoAnterior = tecnico.getEstado();
        String servicioTecnicoAnterior = tecnico.getIdServicioActivo();

        if (solicitud.isPrioritaria()) {
            colaPrioritaria.eliminar(solicitud);
        } else {
            colaOrdinaria.eliminar(solicitud);
        }
        unidad.asignar();
        tecnico.ocupar(solicitud.getId());
        solicitud.asignar(unidad, tecnico);
        serviciosActivos.agregar(solicitud);
        registrarAccion("DESPACHO", "Solicitud " + solicitud.getId() + " asignada a la unidad "
                + unidad.getCodigo() + " y al tecnico " + tecnico.getNombre());
        registrarTransaccion("Despacho de la solicitud " + solicitud.getId(), new Runnable() {
            @Override
            public void run() {
                serviciosActivos.eliminar(solicitud);
                solicitud.revertirAsignacion();
                unidad.setEstado(estadoUnidadAnterior);
                tecnico.restaurar(servicioTecnicoAnterior, estadoTecnicoAnterior);
                colaOrdinaria.cargar(fotoOrdinaria);
                colaPrioritaria.cargar(fotoPrioritaria);
            }
        });
        return solicitud;
    }

    public Solicitud despachoAutomatico() {
        Solicitud solicitud = solicitudSiguiente();
        if (solicitud == null) {
            throw new ReglaNegocioExcepcion("No hay solicitudes pendientes para despachar.");
        }
        UnidadMovil unidad = elegirUnidad(solicitud);
        if (unidad == null) {
            throw new ReglaNegocioExcepcion("No hay unidades disponibles para el despacho.");
        }
        Tecnico tecnico = elegirTecnico(solicitud);
        if (tecnico == null) {
            throw new ReglaNegocioExcepcion("No hay tecnicos disponibles para el despacho.");
        }
        return despachar(solicitud.getId(), unidad.getUuid(), tecnico.getIdentificacion());
    }

    public void marcarAtendida(String idSolicitud) {
        Solicitud solicitud = obtenerSolicitud(idSolicitud);
        if (!solicitud.tieneRecursosAsignados()) {
            throw new ReglaNegocioExcepcion("La solicitud " + idSolicitud
                    + " no puede marcarse atendida sin recursos asignados.");
        }
        if (solicitud.getEstado() == EstadoSolicitud.SOLUCIONADA) {
            throw new ReglaNegocioExcepcion("La solicitud " + idSolicitud + " ya fue solucionada.");
        }
        EstadoSolicitud estadoAnterior = solicitud.getEstado();
        java.time.LocalDateTime fechaAnterior = solicitud.getFechaAtencion();
        solicitud.marcarAtendida();
        registrarAccion("ATENCION", "Solicitud " + idSolicitud + " marcada como atendida");
        registrarTransaccion("Atencion de la solicitud " + idSolicitud, new Runnable() {
            @Override
            public void run() {
                solicitud.revertirAtencion(estadoAnterior, fechaAnterior);
            }
        });
    }

    public void solucionarSolicitud(String idSolicitud) {
        Solicitud solicitud = obtenerSolicitud(idSolicitud);
        if (!solicitud.tieneRecursosAsignados()) {
            throw new ReglaNegocioExcepcion("La solicitud " + idSolicitud
                    + " requiere recursos asignados antes de solucionarse.");
        }
        if (solicitud.getEstado() == EstadoSolicitud.SOLUCIONADA) {
            throw new ReglaNegocioExcepcion(
                    "La solicitud " + idSolicitud + " ya se encuentra solucionada.");
        }
        UnidadMovil unidad = solicitud.getUnidad();
        Tecnico tecnico = solicitud.getTecnico();
        EstadoSolicitud estadoAnterior = solicitud.getEstado();
        java.time.LocalDateTime fechaAtencionAnterior = solicitud.getFechaAtencion();
        EstadoUnidad estadoUnidadAnterior = unidad.getEstado();
        EstadoTecnico estadoTecnicoAnterior = tecnico.getEstado();
        String servicioTecnicoAnterior = tecnico.getIdServicioActivo();
        Lista<Solicitud> fotoActivas = serviciosActivos.copia();
        Lista<Solicitud> fotoSolucionadas = serviciosSolucionados.copia();

        solicitud.marcarSolucionada();
        unidad.liberar();
        tecnico.liberar();
        serviciosActivos.eliminar(solicitud);
        serviciosSolucionados.agregar(solicitud);
        registrarAccion("SOLUCION", "Solicitud " + idSolicitud + " solucionada. Unidad "
                + unidad.getCodigo() + " y tecnico " + tecnico.getNombre() + " liberados.");
        registrarTransaccion("Solucion de la solicitud " + idSolicitud, new Runnable() {
            @Override
            public void run() {
                serviciosActivos.cargar(fotoActivas);
                serviciosSolucionados.cargar(fotoSolucionadas);
                solicitud.revertirSolucion(estadoAnterior, fechaAtencionAnterior);
                unidad.setEstado(estadoUnidadAnterior);
                tecnico.restaurar(servicioTecnicoAnterior, estadoTecnicoAnterior);
            }
        });
    }

    public Transaccion deshacerUltimaTransaccion() {
        if (transacciones.estaVacia()) {
            throw new ReglaNegocioExcepcion("No hay transacciones pendientes por deshacer.");
        }
        Transaccion transaccion = transacciones.desempujar();
        transaccion.deshacer();
        registrarAccion("SUPERVISOR", "Se deshizo la transaccion #"
                + transaccion.getConsecutivo() + ": " + transaccion.getDescripcion());
        return transaccion;
    }

    public Maletin registrarMaletin(String codigo, String contenido) {
        validarTexto(codigo, "El codigo del maletin es obligatorio.");
        validarTexto(contenido, "El contenido del maletin es obligatorio.");
        if (buscarMaletin(codigo) != null) {
            throw new ReglaNegocioExcepcion("Ya existe un maletin con el codigo " + codigo + ".");
        }
        FotoInsumos foto = fotoInsumos();
        Maletin maletin = new Maletin(codigo.trim(), contenido.trim(), EstadoMaletin.DISPONIBLE);
        maletinesDisponibles.empujar(maletin);
        registrarAccion("INSUMOS", "Maletin " + maletin.getCodigo()
                + " apilado en la estanteria de disponibles");
        registrarTransaccion("Registro del maletin " + maletin.getCodigo(), new Runnable() {
            @Override
            public void run() {
                restaurarInsumos(foto);
            }
        });
        return maletin;
    }

    public Maletin despacharMaletin() {
        if (maletinesDisponibles.estaVacia()) {
            throw new ReglaNegocioExcepcion("No hay maletines disponibles en la estanteria.");
        }
        FotoInsumos foto = fotoInsumos();
        Maletin maletin = maletinesDisponibles.desempujar();
        maletin.setEstado(EstadoMaletin.EN_SERVICIO);
        maletinesEnServicio.agregar(maletin);
        registrarAccion("INSUMOS", "Maletin " + maletin.getCodigo()
                + " despachado desde la cima de la estanteria");
        registrarTransaccion("Despacho del maletin " + maletin.getCodigo(), new Runnable() {
            @Override
            public void run() {
                restaurarInsumos(foto);
            }
        });
        return maletin;
    }

    public void enviarMaletinAuditoria(String codigo) {
        Maletin maletin = buscarMaletinEnServicio(codigo);
        if (maletin == null) {
            throw new ReglaNegocioExcepcion(
                    "El maletin " + codigo + " no se encuentra en servicio.");
        }
        FotoInsumos foto = fotoInsumos();
        maletinesEnServicio.eliminar(maletin);
        maletin.setEstado(EstadoMaletin.EN_AUDITORIA);
        maletinesAuditoria.empujar(maletin);
        registrarAccion("INSUMOS", "Maletin " + maletin.getCodigo()
                + " enviado a la estacion de auditoria");
        registrarTransaccion("Envio a auditoria del maletin " + maletin.getCodigo(),
                new Runnable() {
                    @Override
                    public void run() {
                        restaurarInsumos(foto);
                    }
                });
    }

    public Maletin auditarMaletin(boolean aprobado) {
        if (maletinesAuditoria.estaVacia()) {
            throw new ReglaNegocioExcepcion("No hay maletines pendientes de auditoria.");
        }
        FotoInsumos foto = fotoInsumos();
        Maletin maletin = maletinesAuditoria.desempujar();
        if (aprobado) {
            maletin.setEstado(EstadoMaletin.DISPONIBLE);
            maletinesDisponibles.empujar(maletin);
            registrarAccion("INSUMOS", "Maletin " + maletin.getCodigo()
                    + " aprobado y reincorporado a la estanteria");
        } else {
            maletin.setEstado(EstadoMaletin.EN_AUDITORIA);
            maletinesDescartados.agregar(maletin);
            registrarAccion("INSUMOS", "Maletin " + maletin.getCodigo()
                    + " rechazado en auditoria y enviado a reposicion");
        }
        registrarTransaccion("Auditoria del maletin " + maletin.getCodigo(), new Runnable() {
            @Override
            public void run() {
                restaurarInsumos(foto);
            }
        });
        return maletin;
    }

    public Repuesto apilarRepuesto(String codigo, String nombre) {
        validarTexto(codigo, "El codigo del repuesto es obligatorio.");
        validarTexto(nombre, "El nombre del repuesto es obligatorio.");
        if (buscarRepuesto(codigo) != null) {
            throw new ReglaNegocioExcepcion("Ya existe un repuesto con el codigo " + codigo + ".");
        }
        Lista<Repuesto> fotoPila = repuestosDisponibles.listar();
        Lista<Repuesto> fotoDespachados = repuestosDespachados.copia();
        Repuesto repuesto = new Repuesto(codigo.trim(), nombre.trim());
        repuestosDisponibles.empujar(repuesto);
        registrarAccion("INSUMOS", "Repuesto " + repuesto.getCodigo()
                + " apilado para alta rotacion");
        registrarTransaccion("Apilamiento del repuesto " + repuesto.getCodigo(), new Runnable() {
            @Override
            public void run() {
                repuestosDisponibles.cargar(invertir(fotoPila));
                repuestosDespachados.cargar(fotoDespachados);
            }
        });
        return repuesto;
    }

    public Repuesto despacharRepuesto() {
        if (repuestosDisponibles.estaVacia()) {
            throw new ReglaNegocioExcepcion("No hay repuestos disponibles en el almacen.");
        }
        Lista<Repuesto> fotoPila = repuestosDisponibles.listar();
        Lista<Repuesto> fotoDespachados = repuestosDespachados.copia();
        Repuesto repuesto = repuestosDisponibles.desempujar();
        repuestosDespachados.agregar(repuesto);
        registrarAccion("INSUMOS", "Repuesto " + repuesto.getCodigo()
                + " despachado desde la cima del almacen");
        registrarTransaccion("Despacho del repuesto " + repuesto.getCodigo(), new Runnable() {
            @Override
            public void run() {
                repuestosDisponibles.cargar(invertir(fotoPila));
                repuestosDespachados.cargar(fotoDespachados);
            }
        });
        return repuesto;
    }

    public int exportarReporteCSV(String rutaArchivo) throws IOException {
        Lista<Solicitud> delDia = new Lista<Solicitud>();
        LocalDate hoy = LocalDate.now();
        for (int i = 0; i < serviciosSolucionados.tamano(); i++) {
            Solicitud solicitud = serviciosSolucionados.obtener(i);
            if (solicitud.getFechaSolucion() != null
                    && solicitud.getFechaSolucion().toLocalDate().equals(hoy)) {
                delDia.agregar(solicitud);
            }
        }
        int total = ExportadorCSV.exportar(delDia, rutaArchivo);
        registrarAccion("REPORTES", "Reporte CSV generado con " + total
                + " incidencias solucionadas en la jornada");
        return total;
    }

    public void cargarDatosDemostracion() {
        if (!unidades.estaVacia() || !tecnicos.estaVacia()) {
            throw new ReglaNegocioExcepcion(
                    "Los datos de demostracion solo pueden cargarse en un sistema vacio.");
        }
        registrarUnidad("GRU-101", TipoUnidad.GRUA, ZonaOperacion.NORTE,
                NivelDisponibilidad.ALTA);
        registrarUnidad("MOT-204", TipoUnidad.MOTO_RESPUESTA, ZonaOperacion.CENTRO,
                NivelDisponibilidad.ALTA);
        registrarUnidad("UTI-310", TipoUnidad.VEHICULO_UTILITARIO, ZonaOperacion.SUR,
                NivelDisponibilidad.MEDIA);
        registrarUnidad("COC-415", TipoUnidad.COCHE_LIGERO, ZonaOperacion.OCCIDENTE,
                NivelDisponibilidad.MEDIA);
        UnidadMovil unidadTaller = registrarUnidad("GRU-502", TipoUnidad.GRUA,
                ZonaOperacion.PERIFERIA, NivelDisponibilidad.BAJA);
        cambiarEstadoUnidad(unidadTaller.getUuid(), EstadoUnidad.MANTENIMIENTO);

        registrarTecnico("1020456", "Andres Molina", Especialidad.GRUA_REMOLQUE,
                ZonaOperacion.NORTE);
        registrarTecnico("1034877", "Laura Cardenas", Especialidad.ELECTRICIDAD,
                ZonaOperacion.CENTRO);
        registrarTecnico("1042119", "Jorge Penaloza", Especialidad.LLANTAS,
                ZonaOperacion.SUR);
        registrarTecnico("1055630", "Diana Rojas", Especialidad.MECANICA_GENERAL,
                ZonaOperacion.OCCIDENTE);

        registrarCliente("900123456", "Transportes Andinos S.A.", TipoCliente.FLOTA_TRANSPORTE,
                "6015550101", "Avenida Las Americas 45-12");
        registrarCliente("10256789", "Camila Duarte", TipoCliente.PARTICULAR, "3105558899",
                "Calle 74 12-30");
        registrarCliente("830998877", "Aseguradora Sura", TipoCliente.ASEGURADORA,
                "6015550202", "Carrera 7 71-21");
        registrarCliente("860112233", "Hospital Movil Distrital", TipoCliente.SERVICIO_PUBLICO,
                "6015550303", "Calle 1 9-50");

        crearSolicitud("10256789", TipoIncidente.BATERIA_DESCARGADA, ZonaOperacion.CENTRO, 1, 1, 1,
                "Bateria agotada en estacionamiento residencial");
        crearSolicitud("900123456", TipoIncidente.AVERIA_MECANICA, ZonaOperacion.NORTE, 4, 5, 4,
                "Bus de pasajeros varado en la autopista norte");
        crearSolicitud("860112233", TipoIncidente.REMOLQUE, ZonaOperacion.SUR, 5, 5, 5,
                "Ambulancia inmovilizada en la calle 45 sur");
        registrarMaletin("MAL-001", "Kit de bateria y cables de arranque");
        registrarMaletin("MAL-002", "Kit de llantas y herramientas hidraulicas");
        registrarMaletin("MAL-003", "Kit electrico y multimetro");
        apilarRepuesto("REP-001", "Llanta 195/65 R15");
        apilarRepuesto("REP-002", "Bateria 12V 60Ah");
        apilarRepuesto("REP-003", "Bomba de combustible");
    }

    public Lista<UnidadMovil> listarUnidades() {
        Lista<UnidadMovil> lista = unidades.valores();
        lista.ordenar(new Comparador<UnidadMovil>() {
            @Override
            public int comparar(UnidadMovil primero, UnidadMovil segundo) {
                return primero.getCodigo().compareToIgnoreCase(segundo.getCodigo());
            }
        });
        return lista;
    }

    public Lista<UnidadMovil> listarUnidadesDisponibles() {
        Lista<UnidadMovil> lista = new Lista<UnidadMovil>();
        Lista<UnidadMovil> todas = listarUnidades();
        for (int i = 0; i < todas.tamano(); i++) {
            if (todas.obtener(i).estaDisponible()) {
                lista.agregar(todas.obtener(i));
            }
        }
        lista.ordenar(new Comparador<UnidadMovil>() {
            @Override
            public int comparar(UnidadMovil primero, UnidadMovil segundo) {
                return primero.getDisponibilidad().ordinal()
                        - segundo.getDisponibilidad().ordinal();
            }
        });
        return lista;
    }

    public Lista<UnidadMovil> listarUnidadesPorEstado(EstadoUnidad estado) {
        Lista<UnidadMovil> lista = new Lista<UnidadMovil>();
        Lista<UnidadMovil> todas = listarUnidades();
        for (int i = 0; i < todas.tamano(); i++) {
            if (todas.obtener(i).getEstado() == estado) {
                lista.agregar(todas.obtener(i));
            }
        }
        return lista;
    }

    public Lista<UnidadMovil> listarUnidadesEnTaller() {
        Lista<UnidadMovil> lista = new Lista<UnidadMovil>();
        Lista<UnidadMovil> todas = listarUnidades();
        for (int i = 0; i < todas.tamano(); i++) {
            EstadoUnidad estado = todas.obtener(i).getEstado();
            if (estado == EstadoUnidad.EN_TALLER || estado == EstadoUnidad.MANTENIMIENTO) {
                lista.agregar(todas.obtener(i));
            }
        }
        return lista;
    }

    public Lista<Tecnico> listarTecnicos() {
        Lista<Tecnico> lista = tecnicos.valores();
        lista.ordenar(new Comparador<Tecnico>() {
            @Override
            public int comparar(Tecnico primero, Tecnico segundo) {
                return primero.getNombre().compareToIgnoreCase(segundo.getNombre());
            }
        });
        return lista;
    }

    public Lista<Tecnico> listarTecnicosDisponibles() {
        Lista<Tecnico> lista = new Lista<Tecnico>();
        Lista<Tecnico> todos = listarTecnicos();
        for (int i = 0; i < todos.tamano(); i++) {
            if (todos.obtener(i).estaDisponible()) {
                lista.agregar(todos.obtener(i));
            }
        }
        return lista;
    }

    public Lista<Cliente> listarClientes() {
        return clientes.valores();
    }

    public Lista<Solicitud> listarColaOrdinaria() {
        return colaOrdinaria.listar();
    }

    public Lista<Solicitud> listarColaPrioritaria() {
        return colaPrioritaria.listar();
    }

    public Lista<Solicitud> listarServiciosActivos() {
        return serviciosActivos.copia();
    }

    public Lista<Solicitud> listarServiciosSolucionados() {
        return serviciosSolucionados.copia();
    }

    public Lista<Maletin> listarMaletinesDisponibles() {
        return maletinesDisponibles.listar();
    }

    public Lista<Maletin> listarMaletinesAuditoria() {
        return maletinesAuditoria.listar();
    }

    public Lista<Maletin> listarMaletinesEnServicio() {
        return maletinesEnServicio.copia();
    }

    public Lista<Maletin> listarMaletinesDescartados() {
        return maletinesDescartados.copia();
    }

    public Lista<Repuesto> listarRepuestos() {
        return repuestosDisponibles.listar();
    }

    public Lista<Repuesto> listarRepuestosDespachados() {
        return repuestosDespachados.copia();
    }

    public Lista<RegistroAccion> listarHistorial() {
        return historial.copia();
    }

    public Lista<Transaccion> listarTransaccionesPendientes() {
        return transacciones.listar();
    }

    public int contarTransaccionesPendientes() {
        return transacciones.tamano();
    }

    public int contarUnidades(EstadoUnidad estado) {
        return listarUnidadesPorEstado(estado).tamano();
    }

    public int contarTecnicos(EstadoTecnico estado) {
        int total = 0;
        Lista<Tecnico> lista = listarTecnicos();
        for (int i = 0; i < lista.tamano(); i++) {
            if (lista.obtener(i).getEstado() == estado) {
                total++;
            }
        }
        return total;
    }

    public Solicitud obtenerSolicitud(String idSolicitud) {
        Solicitud solicitud = solicitudes.obtener(idSolicitud);
        if (solicitud == null) {
            throw new ReglaNegocioExcepcion(
                    "No existe una solicitud con el identificador " + idSolicitud + ".");
        }
        return solicitud;
    }

    public UnidadMovil obtenerUnidad(String uuid) {
        UnidadMovil unidad = unidades.obtener(uuid);
        if (unidad == null) {
            throw new ReglaNegocioExcepcion("No existe una unidad con el identificador " + uuid + ".");
        }
        return unidad;
    }

    public Tecnico obtenerTecnico(String identificacion) {
        Tecnico tecnico = tecnicos.obtener(identificacion);
        if (tecnico == null) {
            throw new ReglaNegocioExcepcion(
                    "No existe un tecnico con la identificacion " + identificacion + ".");
        }
        return tecnico;
    }

    private UnidadMovil buscarUnidadPorCodigo(String codigo) {
        Lista<UnidadMovil> lista = unidades.valores();
        for (int i = 0; i < lista.tamano(); i++) {
            if (lista.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return lista.obtener(i);
            }
        }
        return null;
    }

    private boolean tieneServicioActivo(UnidadMovil unidad) {
        for (int i = 0; i < serviciosActivos.tamano(); i++) {
            Solicitud solicitud = serviciosActivos.obtener(i);
            if (solicitud.getUnidad() != null
                    && solicitud.getUnidad().getUuid().equals(unidad.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private UnidadMovil elegirUnidad(Solicitud solicitud) {
        Lista<UnidadMovil> disponibles = listarUnidadesDisponibles();
        TipoUnidad preferido = tipoPreferido(solicitud.getTipoIncidente());
        for (int i = 0; i < disponibles.tamano(); i++) {
            UnidadMovil unidad = disponibles.obtener(i);
            if (unidad.getZona() == solicitud.getZona() && unidad.getTipo() == preferido) {
                return unidad;
            }
        }
        for (int i = 0; i < disponibles.tamano(); i++) {
            if (disponibles.obtener(i).getZona() == solicitud.getZona()) {
                return disponibles.obtener(i);
            }
        }
        for (int i = 0; i < disponibles.tamano(); i++) {
            if (disponibles.obtener(i).getTipo() == preferido) {
                return disponibles.obtener(i);
            }
        }
        if (!disponibles.estaVacia()) {
            return disponibles.obtener(0);
        }
        return null;
    }

    private Tecnico elegirTecnico(Solicitud solicitud) {
        Lista<Tecnico> disponibles = listarTecnicosDisponibles();
        Especialidad preferida = especialidadPreferida(solicitud.getTipoIncidente());
        for (int i = 0; i < disponibles.tamano(); i++) {
            if (disponibles.obtener(i).getEspecialidad() == preferida) {
                return disponibles.obtener(i);
            }
        }
        if (!disponibles.estaVacia()) {
            return disponibles.obtener(0);
        }
        return null;
    }

    private TipoUnidad tipoPreferido(TipoIncidente incidente) {
        if (incidente == TipoIncidente.REMOLQUE || incidente == TipoIncidente.AVERIA_MECANICA) {
            return TipoUnidad.GRUA;
        }
        if (incidente == TipoIncidente.PINCHAZO || incidente == TipoIncidente.BATERIA_DESCARGADA) {
            return TipoUnidad.MOTO_RESPUESTA;
        }
        return TipoUnidad.VEHICULO_UTILITARIO;
    }

    private Especialidad especialidadPreferida(TipoIncidente incidente) {
        if (incidente == TipoIncidente.REMOLQUE) {
            return Especialidad.GRUA_REMOLQUE;
        }
        if (incidente == TipoIncidente.PINCHAZO) {
            return Especialidad.LLANTAS;
        }
        if (incidente == TipoIncidente.BATERIA_DESCARGADA) {
            return Especialidad.ELECTRICIDAD;
        }
        if (incidente == TipoIncidente.CERRAJERIA) {
            return Especialidad.CERRAJERIA;
        }
        if (incidente == TipoIncidente.COMBUSTIBLE) {
            return Especialidad.COMBUSTIBLE;
        }
        if (incidente == TipoIncidente.DIAGNOSTICO) {
            return Especialidad.DIAGNOSTICO;
        }
        return Especialidad.MECANICA_GENERAL;
    }

    private Maletin buscarMaletin(String codigo) {
        Lista<Maletin> disponibles = maletinesDisponibles.listar();
        for (int i = 0; i < disponibles.tamano(); i++) {
            if (disponibles.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return disponibles.obtener(i);
            }
        }
        Lista<Maletin> enServicio = maletinesEnServicio.copia();
        for (int i = 0; i < enServicio.tamano(); i++) {
            if (enServicio.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return enServicio.obtener(i);
            }
        }
        Lista<Maletin> auditoria = maletinesAuditoria.listar();
        for (int i = 0; i < auditoria.tamano(); i++) {
            if (auditoria.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return auditoria.obtener(i);
            }
        }
        Lista<Maletin> descartados = maletinesDescartados.copia();
        for (int i = 0; i < descartados.tamano(); i++) {
            if (descartados.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return descartados.obtener(i);
            }
        }
        return null;
    }

    private Maletin buscarMaletinEnServicio(String codigo) {
        for (int i = 0; i < maletinesEnServicio.tamano(); i++) {
            if (maletinesEnServicio.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return maletinesEnServicio.obtener(i);
            }
        }
        return null;
    }

    private Repuesto buscarRepuesto(String codigo) {
        Lista<Repuesto> disponibles = repuestosDisponibles.listar();
        for (int i = 0; i < disponibles.tamano(); i++) {
            if (disponibles.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return disponibles.obtener(i);
            }
        }
        Lista<Repuesto> despachados = repuestosDespachados.copia();
        for (int i = 0; i < despachados.tamano(); i++) {
            if (despachados.obtener(i).getCodigo().equalsIgnoreCase(codigo.trim())) {
                return despachados.obtener(i);
            }
        }
        return null;
    }

    private FotoInsumos fotoInsumos() {
        return new FotoInsumos(maletinesDisponibles.listar(), maletinesAuditoria.listar(),
                maletinesEnServicio.copia(), maletinesDescartados.copia());
    }

    private void restaurarInsumos(FotoInsumos foto) {
        maletinesDisponibles.cargar(invertir(foto.disponibles));
        maletinesAuditoria.cargar(invertir(foto.auditoria));
        maletinesEnServicio.cargar(foto.enServicio);
        maletinesDescartados.cargar(foto.descartados);
        Lista<Maletin> disponibles = maletinesDisponibles.listar();
        for (int i = 0; i < disponibles.tamano(); i++) {
            disponibles.obtener(i).setEstado(EstadoMaletin.DISPONIBLE);
        }
        Lista<Maletin> auditoria = maletinesAuditoria.listar();
        for (int i = 0; i < auditoria.tamano(); i++) {
            auditoria.obtener(i).setEstado(EstadoMaletin.EN_AUDITORIA);
        }
        for (int i = 0; i < maletinesEnServicio.tamano(); i++) {
            maletinesEnServicio.obtener(i).setEstado(EstadoMaletin.EN_SERVICIO);
        }
    }

    private <T> Lista<T> invertir(Lista<T> original) {
        Lista<T> invertida = new Lista<T>();
        for (int i = original.tamano() - 1; i >= 0; i--) {
            invertida.agregar(original.obtener(i));
        }
        return invertida;
    }

    private void registrarAccion(String categoria, String descripcion) {
        historial.agregar(0, new RegistroAccion(categoria, descripcion));
        while (historial.tamano() > MAX_HISTORIAL) {
            historial.eliminar(historial.tamano() - 1);
        }
    }

    private void registrarTransaccion(String descripcion, Runnable reversa) {
        consecutivoTransaccion++;
        transacciones.empujar(new Transaccion(consecutivoTransaccion, descripcion, reversa));
        if (transacciones.tamano() > MAXIMO_TRANSACCIONES) {
            Lista<Transaccion> lista = transacciones.listar();
            lista.eliminar(lista.tamano() - 1);
            transacciones.cargar(invertir(lista));
        }
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ReglaNegocioExcepcion(mensaje);
        }
    }

    private void validarRango(int valor, String campo) {
        if (valor < 1 || valor > 5) {
            throw new ReglaNegocioExcepcion(campo + " debe estar entre 1 y 5.");
        }
    }

    private static class FotoInsumos {

        private Lista<Maletin> disponibles;
        private Lista<Maletin> auditoria;
        private Lista<Maletin> enServicio;
        private Lista<Maletin> descartados;

        private FotoInsumos(Lista<Maletin> disponibles, Lista<Maletin> auditoria,
                Lista<Maletin> enServicio, Lista<Maletin> descartados) {
            this.disponibles = disponibles;
            this.auditoria = auditoria;
            this.enServicio = enServicio;
            this.descartados = descartados;
        }
    }
}
