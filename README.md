# RayoKross Academy

## 👥 Miembros del Equipo
| Nombre y Apellidos | Correo URJC | Usuario GitHub |
|:--- |:--- |:--- |
| Omar Ba Diallo | oa.ba.2024@alumnos.urjc.es | MalcomJrr |
| Daniel Fernández Tomé | d.fernandezt.2024@alumnos.urjc.es | danicroko |
| Ángel Menéndez Leyenda | a.menendez.2024@alumnos.urjc.es | angelmnndez |
| Gonzalo Roig López | g.roig.2024@alumnos.urjc.es | groig-0 |

---

## 🎭 **Preparación: Definición del Proyecto**

### **Descripción del Tema**
Esta aplicación web consiste en una plataforma de formación online orientada al ámbito de la ciberseguridad diseñada para gestionar cursos académicos. El sistema permite a los profesores publicar contenido educativo y a los alumnos matricularse para acceder a dicho material. A su vez, permite al usuario acceder a recursos educativos (como imágenes y vídeos de apoyo) y el progreso del alumno.

### **Entidades**
Indicar las entidades principales que gestionará la aplicación y las relaciones entre ellas:

1. Usuario: Representa a las personas que interactúan con la web (Alumnos y Profesores/Administradores).
2. Curso: Representa el curso que se imparte (ej: "Curso de redes").
3. Recuros académicos: Unidad de contenido dentro de un curso (ej: "Tema 1: Variables"). Aquí es donde se alojarán los materiales.
4. Matrícula: Representa la inscripción de un alumno en un curso específico.

**Relaciones entre entidades:**
- Usuario - Matrícula: Un usuario (alumno) puede tener múltiples matrículas (1:N).
- Curso - Matrícula: Un curso puede tener múltiples matrículas asociadas a distintos alumnos (1:N).
- Curso - Lección: Un curso se compone de múltiples lecciones, pero una lección pertenece a un único curso (1:N).
- Usuario - Curso: Un usuario (profesor) puede crear/ser dueño de múltiples cursos (1:N).

### **Permisos de los Usuarios**
La aplicación distingue tres roles con permisos diferenciados sobre los datos:

* **Usuario Anónimo**: 
  - Permisos: Visualización del catálogo público de cursos, búsqueda de cursos por nombre o categoría, acceso a la página de login y registro.
  - No es dueño de ninguna entidad

* **Usuario Registrado**: 
  - Permisos: Todo lo del anónimo más: capacidad para matricularse en cursos, acceso al contenido detallado (lecciones) solo de los cursos donde esté matriculado, edición de su propio perfil.
  - Es dueño de: Su Perfil de Usuario (puede editar sus datos y foto) y sus Matrículas (puede cancelar su propia matrícula).

* **Administrador**: 
  - Permisos: Gestión completa de la plataforma. Puede crear nuevos cursos, añadir lecciones a los cursos, eliminar usuarios y visualizar todas las matrículas.
  - Es dueño de: Todos los Cursos y Lecciones creados en la plataforma.

### **Imágenes**
Se cumple el requisito de subida de imágenes en las siguientes entidades:

- Usuario: Cada usuario podrá subir una imagen de avatar o perfil.
- Curso: Cada curso tendrá una imagen de portada representativa que se mostrará en el catálogo.

---

## 🛠 **Práctica 1: Maquetación de páginas con HTML y CSS**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Diagrama de Navegación**
Diagrama que muestra cómo se navega entre las diferentes páginas de la aplicación:

![Diagrama de Navegación](images/Diagrama_web.png)

> Flujo de Navegación

Acceso Público: El usuario llega a la Home, explora el Catálogo o consulta el Detalle de los 3 cursos destacados.

Autenticación: Mediante Login/Registro, el sistema identifica al usuario.

Zona Privada: El flujo redirige al Perfil (gestión del alumno), desde donde se accede al Aula para ver lecciones.

Gestión (Admin): Desde el perfil, los usuarios autorizados saltan al Dashboard de Admin para gestionar cursos y usuarios.

Esquema: Home → Login → Perfil → Admin.

### **Capturas de Pantalla y Descripción de Páginas**

#### **1. Página Principal / Home**
![Página Principal](images/index.png)

> Página de inicio que muestra los productos destacados. Incluye barra de navegación y acceso a registro/login para usuarios no autenticados.

#### **2. Página Login / Iniciar sesión**
![Página Principal](images/login.png)

> Página de inicio de sesión que permite a usuarios previamente registrados autenticarse.

#### **3. Página Register / Registrarse**
![Página Principal](images/register.png)

> Página de registro que permite a nuevos usuarios crearse un usuario.

#### **4. Página Cursos / Catálogo**
![Página Principal](images/catalogo.png)

> Página que muestra todos los cursos disponibles para cursar.

#### **5. Página Detalle curso**
![Página Principal](images/detalle.png)

> Página que muestra las características del curso como duración, contenido. Además de la opción para poder comprar el curso

#### **6. Página Profile / Perfil**
![Página Principal](images/perfil.png)

> Página que muestra el perfil de un usuario autenticado que permite ver los datos, los cursos en los que está matriculado pudiendo desmatricularse, cambiar la foto de perfil, acceder al panel de administración (en caso de ser Administrador) y cerrar sesión

#### **7. Página Admin / Administrador**
![Página Principal](images/admin.png)

> Página única para administradores que permite la creación de nuevos cursos y administrarlos.

### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - Omar Ba Diallo**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Página principal creada](URL_commit_1)  | [Archivo1](index.html)   |
|2| [Página de login creada](URL_commit_2)  | [Archivo2](login.html)   |
|3| [Página de register creada](URL_commit_3)  | [Archivo3](register.html)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](#)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - Daniel Fernández Tomé**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| ([Descripcion del curso creada](https://github.com/DWS-2026/dws-2026-project-base/commit/93db7dcec9628f109bb27726831706979c3db893)) | [courseDescription.html]   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - Ángel Menéndez Leyenda**

Maquetación de Perfil, Admin y estilos CSS

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| ([URL_commit_1](https://github.com/DWS-2026/dws-2026-project-base/commit/563fabc9533ad550eed6473b74855a34ed29a1b0))  | [admin_dashboard.html][css/styles.css][perfil.html]|
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - Gonzalo Roig López**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Catálogo de cursos creado](https://github.com/DWS-2026/dws-2026-project-base/commit/00dc2b86925732d96ea741494bc25bb1e774efe8))  | [courses.html] [css/styles.css])   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 2: Web con HTML generado en servidor**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Navegación y Capturas de Pantalla**

#### **Diagrama de Navegación**

Solo si ha cambiado.

#### **Capturas de Pantalla Actualizadas**

Solo si han cambiado.

### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.8 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/[usuario]/[nombre-repositorio].git
   cd [nombre-repositorio]
   ```

2. **AQUÍ INDICAR LO SIGUIENTES PASOS**

#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin`, contraseña: `admin`
- **Usuario Registrado**: usuario: `user`, contraseña: `user`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

![Diagrama Entidad-Relación](images/database-diagram.png)

> [Descripción opcional: Ej: "El diagrama muestra las 4 entidades principales: Usuario, Producto, Pedido y Categoría, con sus respectivos atributos y relaciones 1:N y N:M."]

### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

![Diagrama de Clases](images/classes-diagram.png)

> [Descripción opcional del diagrama y relaciones principales]

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 3: Incorporación de una API REST a la aplicación web, análisis de vulnerabilidades y contramedidas**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://www.youtube.com/watch?v=x91MPoITQ3I)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Documentación de la API REST**

#### **Especificación OpenAPI**
📄 **[Especificación OpenAPI (YAML)](/api-docs/api-docs.yaml)**

#### **Documentación HTML**
📖 **[Documentación API REST (HTML)](https://raw.githack.com/[usuario]/[repositorio]/main/api-docs/api-docs.html)**

> La documentación de la API REST se encuentra en la carpeta `/api-docs` del repositorio. Se ha generado automáticamente con SpringDoc a partir de las anotaciones en el código Java.

### **Diagrama de Clases y Templates Actualizado**

Diagrama actualizado incluyendo los @RestController y su relación con los @Service compartidos:

![Diagrama de Clases Actualizado](images/complete-classes-diagram.png)

#### **Credenciales de Usuarios de Ejemplo**

| Rol | Usuario | Contraseña |
|:---|:---|:---|
| Administrador | admin | admin123 |
| Usuario Registrado | user1 | user123 |
| Usuario Registrado | user2 | user123 |

### **Participación de Miembros en la Práctica 3**

#### **Alumno 1 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 3 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - [Nombre Completo]**

[Descripción de las tareas y responsabilidades principales del alumno en el proyecto]

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Descripción commit 1](URL_commit_1)  | [Archivo1](URL_archivo_1)   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |
