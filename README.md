# Spring_Boot_Todo_BackEnd

| Action                         | Method   | URL Pattern                       |
| ------------------------------ | -------- | --------------------------------- |
| Retrieve All ToDos             | `GET`    | `/v1/users/{username}/todos`      |
| Retrieve Single Specified ToDo | `GET`    | `/v1/users/{username}/todos/{id}` |
| Delete Specified Single Todo   | `DELETE` | `/v1/users/{username}/todos/{id}`  |
| Update Single Specified Todo   | `PUT`    | `/v1/users/{username}/todos/{id}` |
| Create Single Todo             | `POST`   | `/v1/users/{username}/todos`      |

```java
@RestController
@RequestMapping("/v1") // versioning
// ...
// Retrieve All ToDos:
    @GetMapping("/users/{username}/todos")
/// ...
// Retrieve Single Specified ToDo:
    @GetMapping("/users/{username}/todos/{id}")
/// ...
// Delete Specified Single Todo
    @DeleteMapping("/users/{username}/todos{id}")
/// ...
// Update Single Specified Todo
    @PutMapping("/users/{username}/todos/{id}")
/// ...
// Create Single Todo
    @PostMapping("/users/{username}/todos")
/// ...
```
