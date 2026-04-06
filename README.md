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
📹 **[Enlace al vídeo en YouTube](https://youtu.be/eTdlfR7YGfs)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Diagrama de Navegación**
Diagrama que muestra cómo se navega entre las diferentes páginas de la aplicación:

![Diagrama de Navegación](images/Diagrama_webv2.png)

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
![Página Login](images/login.png)

> Página de inicio de sesión que permite a usuarios previamente registrados autenticarse.

#### **3. Página Register / Registrarse**
![Página Register](images/register.png)

> Página de registro que permite a nuevos usuarios crearse un usuario.

#### **4. Página Cursos / Catálogo**
![Página Cursos](images/catalogov2.png)

> Página que muestra todos los cursos disponibles para cursar.

#### **5. Página Detalle curso**
![Página Detalle](images/detallev2.png)

> Página que muestra las características del curso como duración, contenido. Además de la opción para poder comprar el curso.

#### **6. Página Profile / Perfil**
![Página Perfil](images/perfil.png)

> Página que muestra el perfil de un usuario autenticado que permite ver los datos, los cursos en los que está matriculado pudiendo desmatricularse, cambiar la foto de perfil, acceder al panel de administración (en caso de ser Administrador) y cerrar sesión

#### **7. Página Admin / Administrador**
![Página Admin](images/adminv2.jpeg)

> Página única para administradores que permite la creación de nuevos cursos y administrarlos.

### **Participación de Miembros en la Práctica 1**

#### **Alumno 1 - Omar Ba Diallo**

Realización y diseño de la página principal, la página de registro y la página de inicio de sesión

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Creación de la página principal, login y registro](https://github.com/DWS-2026/dws-2026-project-base/commit/41b1ca27f74afa8e673aa28f5e37fcc67480eed5)  | [Archivo1](index.html)   |
|2| [Actualización para la navegación de los botones](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Página de register creada](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](#)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 2 - Daniel Fernández Tomé**

Creación de la página de descripción de los cursos y diseño de la barra de navegación de la web

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
|1| ([Perfil y Panel admin](https://github.com/DWS-2026/dws-2026-project-base/commit/563fabc9533ad550eed6473b74855a34ed29a1b0))  | [admin_dashboard.html][css/styles.css][perfil.html]|
|2| [Ampliacion del panel de admin con una lista para visualizar alumnos y adminsitradores]([URL_commit_2](https://github.com/DWS-2026/dws-2026-project-base/commit/d6c6bbe206def450a190debfbb7c2d0cfc022403))  | [admin_dashboard.html]   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

#### **Alumno 4 - Gonzalo Roig López**

Creación y diseño de la página que contiene todos los cursos disponibles

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| ([Catálogo de cursos creado](https://github.com/DWS-2026/dws-2026-project-base/commit/00dc2b86925732d96ea741494bc25bb1e774efe8))  | [courses.html] [css/styles.css]   |
|2| [Descripción commit 2](URL_commit_2)  | [Archivo2](URL_archivo_2)   |
|3| [Descripción commit 3](URL_commit_3)  | [Archivo3](URL_archivo_3)   |
|4| [Descripción commit 4](URL_commit_4)  | [Archivo4](URL_archivo_4)   |
|5| [Descripción commit 5](URL_commit_5)  | [Archivo5](URL_archivo_5)   |

---

## 🛠 **Práctica 2: Web con HTML generado en servidor**

### **Vídeo de Demostración**
📹 **[Enlace al vídeo en YouTube](https://youtu.be/MU4uhTDj5s0)**
> Vídeo mostrando las principales funcionalidades de la aplicación web.

### **Navegación y Capturas de Pantalla**

#### **Diagrama de Navegación**
Diagrama que muestra cómo se navega entre las diferentes páginas de la aplicación:

![Diagrama de Navegación](images/Diagrama2.png)

> Flujo de Navegación

PUBLICO:
Home → Catálogo → Detalle Curso
Home → Login 
Home → Registro
Catálogo → Login

AUTENTICADO (Alumno):
Login → Home → Perfil 
Home → Mis Cursos → Curso

ADMIN:
Home → Dashboard Admin
Dashboard → Gestión Cursos → Crear/Editar/Matrículas
Dashboard → Gestión Usuarios → Detalles/Editar
#### **Capturas de Pantalla Actualizadas**

#### **1. Página Principal / Home (Visitante)**
![Página Principal](images/index.png)
> Página de inicio genérica para usuarios no autenticados. Muestra los cursos destacados y ofrece acceso rápido a las páginas de registro e inicio de sesión desde la barra de navegación.

#### **2. Página Principal / Home (Usuario Logueado)**
![Página Principal Usuario](images/MainUser.png)
> Vista de la misma página de inicio, pero adaptada para un usuario estándar que ha iniciado sesión. La barra de navegación se actualiza para mostrar el acceso a su perfil y al carrito de compras.

#### **3. Página Principal / Home (Administrador)**
![Página Principal Admin](images/MainAdmin.png)
> Vista de la página de inicio cuando el usuario autenticado tiene el rol de Administrador. Incluye las opciones de navegación necesarias para acceder directamente al panel de gestión (dashboard).

#### **4. Página Login / Iniciar sesión**
![Página Login](images/Login.png)
> Página de inicio de sesión que permite a usuarios previamente registrados autenticarse en la plataforma.

#### **5. Página Register / Registrarse**
![Página Register](images/Register.png)
> Página de registro que permite a nuevos usuarios crearse una cuenta proporcionando sus datos personales.

#### **6. Página Detalle del Curso**
![Página Detalle Curso](images/CourseDetails.png)
> Página que muestra las características específicas de un curso (descripción, temario, precio) y ofrece la opción de añadirlo al carrito de compras.

#### **7. Página Contenido del Curso (Reproductor)**
![Página Contenido Curso](images/CourseContent.png)
> Interfaz del reproductor donde el alumno, una vez matriculado, puede visualizar las lecciones en vídeo y el contenido del curso.

#### **8. Página Carrito de Compras**
![Página Carrito](images/Cart.png)
> Vista del carrito donde el usuario puede revisar los cursos que ha seleccionado, ver el precio total y proceder al pago/checkout.

#### **9. Página Profile / Perfil de Usuario**
![Perfil de Usuario](images/UserProfile.png)
> Panel personal del usuario estándar donde puede editar su información, cambiar su avatar y visualizar o cancelar los cursos en los que está actualmente matriculado.

#### **10. Página Profile / Perfil de Administrador**
![Perfil de Administrador](images/AdminProfile.png)
> Perfil adaptado para el administrador, mostrando sus datos personales junto con los accesos directos necesarios para gestionar la academia.

#### **11. Panel de Control / Dashboard (Admin)**
![Dashboard Admin](images/AdminDashboard.png)
> Panel principal de administración que centraliza el acceso a la gestión integral de la plataforma (usuarios, cursos y matrículas).

#### **12. Gestión de Usuarios (Admin)**
![Gestión de Usuarios](images/UserManagement.png)
> Vista de administración que despliega una lista completa de todos los alumnos registrados en la base de datos de la plataforma.

#### **13. Edición de Usuario (Admin)**
![Edición de Usuario](images/User%20edited%20by%20admin.png)
> Formulario específico del panel de administración que permite al administrador modificar los datos de un alumno en concreto.

#### **14. Gestión de Matrículas (Admin)**
![Gestión de Matrículas](images/EnrollmentManagement.png)
> Panel de administración diseñado para revisar, añadir o eliminar las matriculaciones de los alumnos en los distintos cursos disponibles.

#### **15. Edición de Curso (Admin)**
![Edición de Curso](images/EditCourse.png)
> Herramienta del panel de control que permite al administrador modificar la información, precio y temario de un curso existente en el catálogo.

### **Instrucciones de Ejecución**

#### **Requisitos Previos**
- **Java**: versión 21 o superior
- **Maven**: versión 3.8 o superior
- **MySQL**: versión 8.0 o superior
- **Git**: para clonar el repositorio

#### **Pasos para ejecutar la aplicación**

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/DWS-2026/project-grupo-7.git
   cd project-grupo-7
   ```

2. **Crear Schema en MySQL**
    Crear Schema con nombre "posts"
3.  **Lanzar app desde VS Code**
#### **Credenciales de prueba**
- **Usuario Admin**: usuario: `admin@rayokross.com`, contraseña: `adminpass1234`
- **Usuario Registrado**: usuario: `student@rayokross.com`, contraseña: `student1234`

### **Diagrama de Entidades de Base de Datos**

Diagrama mostrando las entidades, sus campos y relaciones:

![Diagrama Entidad-Relación](images/DiagramaER.png)

### **Diagrama de Clases y Templates**

Diagrama de clases de la aplicación con diferenciación por colores o secciones:

![Diagrama de Clases](images/DiagramaClases.jpeg)

### **Participación de Miembros en la Práctica 2**

#### **Alumno 1 - Omar Ba Diallo**

Diseño e implementación de las entidades como Enrollment.java y Lesson.java, configurando sus atributos.

Implementación de CartService con alcance de sesión para gestionar la lógica de selección de cursos, cálculo dinámico de precios y vaciado tras la compra.

Creación de un controlador de excepciones (CustomErrorController) para capturar y manejar errores HTTP (400, 403, 404, 500) devolviendo las correspondientes vistas.

Servicios y Controladores: Creación de LessonService, EnrollmentService y la lógica de sus respectivos controladores.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Implementación de la Entidad Enrollment (Matriculación)](https://github.com/DWS-2026/project-grupo-7/commit/c86e075bd523827162ca7a06064d8dae0bd51505)  | [Enrollment.java](https://github.com/DWS-2026/project-grupo-7/blob/main/src/main/java/com/rayokross/academy/models/Enrollment.java)   |
|2| [Creación de plantillas para manejo de Errores HTTP personalizados](https://github.com/DWS-2026/project-grupo-7/commit/25b17b6492d24e5fcf25254b3d79bb885f01ef9c)  | [/error](https://github.com/DWS-2026/project-grupo-7/tree/main/src/main/resources/templates/error)   |
|3| [Desarrollo de la lógica del carrito de la compra](https://github.com/DWS-2026/project-grupo-7/commit/cac74c740e2b4782d1bda0ec2769908760faeb16)  | [CartController.java](https://github.com/DWS-2026/project-grupo-7/blob/main/src/main/java/com/rayokross/academy/controllers/CartController.java)   |
|4| [Creación de la Entidad Lesson](https://github.com/DWS-2026/project-grupo-7/commit/0e7e1a240c8cbbc2d423b50605fe568f934bb1bc)  | [Lesson.java](https://github.com/DWS-2026/project-grupo-7/blob/main/src/main/java/com/rayokross/academy/models/Lesson.java)   |
|5| [Implementación de la capa de Servicio para Lecciones](https://github.com/DWS-2026/project-grupo-7/commit/340d3d909bb5f24c5a74bc111dc610a8ec7c5774)  | [LessonService.java](https://github.com/DWS-2026/project-grupo-7/blob/main/src/main/java/com/rayokross/academy/services/LessonService.java)   |

---

#### **Alumno 2 - Daniel Fernandez Tome**

Creación de controladores: UserController, CourseController, AdminUserController, AuthController, MainController.

Gestión de base de datos y modelos: Configuración de relaciones en entidades como Course.java, gestión de validaciones en User.java, y migración de la base de datos a MySQL.

Servicios: Implementación de métodos de búsqueda y borrado en UserService y formateo de fechas en Enrollment.java.

Seguridad: Creación del AuthController, gestión de contraseñas en app.properties, restricciones entre admins y users, modificación de vista del perfil de usuario desde el admin.

Gestión de inscripciones: Funcionalidades de añadir y eliminar usuarios de los cursos.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Rating deleted](https://github.com/DWS-2026/dws-2026-project-base/commit/a60e2d8ca595241d938b560bad972fd79b7c87ce)  | [DataBaseInitializer](‎src/main/java/com/rayokross/academy/DatabaseInitializer.java‎)   |
|2| [AdminUserController added](https://github.com/DWS-2026/dws-2026-project-base/commit/7bcc81ce54cd72dde51bea096bd5ad27413d9325))  | [AdminUserController](src/main/java/com/rayokross/academy/controllers/AdminUserController.java)   |
|3| [AuthController created](https://github.com/DWS-2026/dws-2026-project-base/commit/ef4db4151a7505beddc336b7bcb49046e49b4bbf)  | [AuthController](src/main/java/com/rayokross/academy/controllers/AuthController.java)   |
|4| [relations added and getters and setters modified in course.java](https://github.com/DWS-2026/dws-2026-project-base/commit/5f175d5e3a3732aba45d0931e6ffdfd71294decb)  | [Course.java](‎src/main/java/com/rayokross/academy/models/Course.java)   |
|5| [UserController created](https://github.com/DWS-2026/dws-2026-project-base/commit/5dba9c02f20c24875c71f127cc671307930fd6b4)  | [UserController](src/main/java/com/rayokross/academy/controllers/UserController.java)   |

---

#### **Alumno 3 - Angel Menendez Leyenda**

Entidades y Lógica de Negocio: Diseño de entidades (Course, User, Enrollment, Cart), controladores y servicios para la gestión de catálogo, matrículas y flujo del carrito.

Seguridad y Control de Acceso: Configuración de SecurityConfig con protección CSRF y gestión de permisos basada en roles (Admin/User).

Frontend Dinámico: Implementación de vistas con Mustache y partials reutilizables para el panel de administración, perfiles y reproductor de cursos.

Gestión de Errores y Validaciones: Creación de un controlador global de excepciones (CustomErrorController) y lógica de validación para precios y subida de archivos.

Configuración del Sistema: Administración de application.properties, inicialización automatizada de la base de datos y gestión del keystore.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [CoursePlayerController and course-player.html](https://github.com/DWS-2026/project-grupo-7/commit/9c7a7692a8e9a1acb4e812c5c26c84740e5cab80) | [CoursePlayerController.java](CoursePlayerController.java) |
|2| [CartController added](https://github.com/DWS-2026/project-grupo-7/commit/7a710e2cfd4e927c2d9da6f99f28b2b011b8812c) | [CartController.java](CartController.java) |
|3| [inclusión AdminCourseController, modificacion en el CourseService](https://github.com/DWS-2026/project-grupo-7/commit/8769a92065c943c5ac7c598b4532c1485b2a1ba2) | [AdminCourseController.java](AdminCourseController.java) |
|4| [User Service Class and User Repository Class](https://github.com/DWS-2026/project-grupo-7/commit/b385353441c1042067b4eed9ec9989d22f1b4ae2) | [UserService.java](UserService.java) |
|5| [403.html and CustomErrorController](https://github.com/DWS-2026/project-grupo-7/commit/d827d4efa953153b101b5c9e054ef74308a056d5) | [CustomErrorController.java](CustomErrorController.java) |

---

#### **Alumno 4 - Gonzalo Roig López**

Creación de imágenes para los cursos en course_description, el index y el catálogo de cursos, además de una paginación ene el catálogo de cursos. Validación de las imágenes añadidas.

Creación del sistema del carrito creando CartController, CartService y cart.html y demás correcciones en este sistema.

Encargado de añadir y configurar Spring Security, crando SecurityConfig.java y RepositoryUserDetailsService, cifrado de contraseñas implementando BCrypt tanto en UserService como en DataBaseInitializer, prevención a ataques XSS y creación de keystore y configuración en application.properties para admitir conexiones https.

Validación de campos en login, register y en el panel de administrador.

| Nº    | Commits      | Files      |
|:------------: |:------------:| :------------:|
|1| [Add pagination in courses and add images](https://github.com/DWS-2026/project-grupo-7/commit/d8c39a2a6f5ed00a5643f6325d71444f74af0c53) | [CourseController.java](CourseController.java) |
|2| [fix validation and deny negative prices and files in image of course in admin dashboard](https://github.com/DWS-2026/project-grupo-7/commit/79b3359e2af7dc7505131bc571f7419f6770d57d) | [AdminCourseController.java](AdminCourseController.java) |
|3| [CartController and CartService](https://github.com/DWS-2026/project-grupo-7/commit/4c90b54fa73f67b8cdbedc22253bd5fe07493a02) | [CartController.java](CartController.java) |  [CartService.java](CartService.java) |
|4| [Add manual validation for registration and login error handling](https://github.com/DWS-2026/project-grupo-7/commit/5ac274dd7b87aa0e9ddf092d6a20e46a6bf69ad4) | [AuthController.java](AuthController.java) |
|5| [SecurityConfig added](https://github.com/DWS-2026/project-grupo-7/commit/e8fddb7824cc1b96a298234b39a6995e0987d613) | [SecurityConfig.java](SecurityConfig.java) |

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
