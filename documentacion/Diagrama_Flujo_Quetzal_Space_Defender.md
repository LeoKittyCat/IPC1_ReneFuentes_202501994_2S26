# Diagrama de flujo — Quetzal Space Defender

**Práctica 2 · IPC1**  
**Estudiante:** René Fuentes · **Carné:** 202501994

Los procesos principales están separados en dos diagramas para que se puedan leer. **A** lleva del menú a la partida y **B** devuelve al menú después de registrar el resultado. La partida termina al perder las **tres vidas**; no se usa un cronómetro de 60 segundos. El top se ordena por puntaje, mientras que el historial conserva el orden de las partidas.

## 1. Menú, pilotos y resultados

```mermaid
flowchart TD
    I([Inicio]) --> M[Mostrar menú principal]
    M --> O{¿Qué opción se eligió?}
    O -->|Crear piloto| D[/Ingresar nombre y elegir nave/]
    D --> V{¿Nombre válido?}
    V -->|No| E[/Mostrar error y corregir/] --> D
    V -->|Sí| G[Guardar piloto] --> M
    O -->|Jugar| S[/Seleccionar piloto y nave/] --> A((A))
    B((B)) --> M
    O -->|Top de puntajes| T[/Mostrar top, gráfica y pestaña Historial/]
    T --> R{¿Generar reporte?}
    R -->|Sí| X[Crear HTML y gráfica PNG] --> C[Cerrar resultados]
    R -->|No| C
    C --> M
    O -->|Salir| F([Fin])
    classDef term fill:#233E58,color:#fff,stroke:#233E58;
    classDef proc fill:#E8F1FB,stroke:#233E58;
    classDef input fill:#E6F5F2,stroke:#233E58;
    classDef decide fill:#FFF2D7,stroke:#233E58;
    classDef conn fill:#EDE9FA,stroke:#233E58;
    class I,F term;
    class M,G,X,C proc;
    class D,E,S,T input;
    class O,V,R decide;
    class A,B conn;
```

## 2. Partida y registro del resultado

```mermaid
flowchart TD
    A((A)) --> IN[Puntaje = 0; vidas = 3; aplicar velocidad y recarga de la nave]
    IN --> UP[Mover nave y actualizar escena; enemigos y proyectiles usan hilos]
    UP --> EV{¿Qué sucede en la partida?}
    EV -->|Nada todavía| UP
    EV -->|Proyectil impacta enemigo| SH[Desactivar ambos; no suma puntos] --> UP
    EV -->|Enemigo toca nave| VI[Restar una vida] --> Z{¿Vidas en cero?}
    Z -->|No| UP
    Z -->|Sí| REG[Registrar partida: piloto, nave, puntaje y fecha] --> B((B))
    EV -->|Toca un objeto| OB{¿Cuál objeto tocó la nave?}
    OB -->|Quaffle| Q[Sumar 10 puntos] --> UP
    OB -->|Snitch| SN[Sumar 150 y eliminar enemigos visibles] --> UP
    OB -->|Bludger| BL[Bloquear nave 2 segundos] --> UP
    classDef proc fill:#E8F1FB,stroke:#233E58;
    classDef decide fill:#FFF2D7,stroke:#233E58;
    classDef conn fill:#EDE9FA,stroke:#233E58;
    class A,B conn;
    class IN,UP,SH,VI,REG,Q,SN,BL proc;
    class EV,Z,OB decide;
```

**Figuras usadas:** óvalo = inicio o fin; rectángulo = proceso; paralelogramo = entrada o salida; rombo = decisión; círculo con letra = conexión entre las dos partes del diagrama. Cada respuesta de una decisión indica hacia dónde continúa el programa.

**Versión lista para imprimir:** [Diagrama de flujo en PDF](Diagrama_Flujo_Quetzal_Space_Defender.pdf).
