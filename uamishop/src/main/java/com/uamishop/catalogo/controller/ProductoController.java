@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping
    public Producto crear(@RequestBody ProductoRequest request) {
        return productoService.crearProducto(
                request.getNombre(),
                request.getDescripcion(),
                request.getPrecio(),
                request.getCategoria()
        );
    }
}
