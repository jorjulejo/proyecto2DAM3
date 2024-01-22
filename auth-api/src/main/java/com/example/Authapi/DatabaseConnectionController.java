@RestController
public class DatabaseConnectionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/check-db-connection")
    public String checkDbConnection() {
        try {
            // Ejecuta una consulta simple para probar la conexión
            String sql = "SELECT 1 FROM DUAL";
            jdbcTemplate.queryForObject(sql, Integer.class);
            return "Conexión a la base de datos Oracle: EXITOSA";
        } catch (Exception e) {
            return "Conexión a la base de datos Oracle: FALLIDA - " + e.getMessage();
        }
    }
}
