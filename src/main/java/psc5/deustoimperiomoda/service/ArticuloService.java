package psc5.deustoimperiomoda.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import psc5.deustoimperiomoda.DataDomain.Articulo;
import psc5.deustoimperiomoda.DataDomain.Categoria;
import psc5.deustoimperiomoda.dao.ArticuloRepository;

@Service
public class ArticuloService {

private ArticuloRepository articuloRepository;
private String connectionString;

    public ArticuloService(ArticuloRepository articuloRepository1){
        this.articuloRepository = articuloRepository1;
        connectionString = "jdbc:sqlite:DeustoImperioModa.db";
        loadDatos();
    }
    
    /** @brief Carga todos los artículos de la base de datos y los guarda en el repositorio de artículos
     */

    public void loadDatos(){
        String sql = "SELECT * FROM articulo";
		
		try (Connection con = DriverManager.getConnection(connectionString);
		    PreparedStatement pStmt = con.prepareStatement(sql)) {	
			
			ResultSet rs = pStmt.executeQuery();
           
			while(rs.next()) {
				Articulo articulo = new Articulo(Categoria.valueOf(rs.getString("categoria")), rs.getString("descripcion"), rs.getString("nombre"), rs.getDouble("precio"), rs.getString("tamano"));
                articulo.setId(rs.getInt("id_art"));
				articuloRepository.save(articulo);
			}
		} catch (SQLException e) {
		}
    }

    /** @brief Coge el artículo por el id
     *  @return Devuelve el artículo si coincide con el id
     */

    public Articulo getArticulo(int id){
        Articulo result = articuloRepository.findById(id);
        
        return result;
    }

    /** @brief Coge todos los artículos
     *  @return Lista de los artículos del repositorio
     */

    public List<Articulo> getAllArticulos(){
        return articuloRepository.findAll();
    }
    
    /** @brief Coge todos los artículos según la categoría enviada
     *  @return Lista de los artículos que coinciden con la categoría enviada
     */

    public List<Articulo> getByCategoria(Categoria categoria){
        
        List<Articulo> articulos = getAllArticulos();
        List<Articulo> result = new ArrayList<Articulo>();

        for (Articulo articulo : articulos) {
            if (articulo.getCategoria().equals(categoria)) {
                result.add(articulo);
            }
        }

        return result.isEmpty() ? null : result;
    }


    /** @brief Añade un artículo al repositorio
     *  @retval True Artículo correctamente añadido
     *  @retval False Artículo no añadido
     */

    public Articulo addArticulo(Articulo articulo){
        return articuloRepository.save(articulo);
    }

    /** @brief Actualiza un artículo del id enviado
     *  @return El artículo actualizado
     */

    public Articulo updateArticulo(Articulo articulo, int id){
    Articulo result = articuloRepository.findById(id);
        if (!(result == null)) {
            
            Articulo updatedArticulo = result;

            updatedArticulo.setDescripcion(articulo.getDescripcion());
            updatedArticulo.setCategoria(articulo.getCategoria());
            updatedArticulo.setNombre(articulo.getNombre());
            updatedArticulo.setPrecio(articulo.getPrecio());
            updatedArticulo.setTamano(articulo.getTamano());
            
            articuloRepository.save(updatedArticulo);

            return result;
        }
        return result;
    }

    /** @brief Boora un artículo por el id
     */

    public void deleteArticulo(int id){
        Articulo result = articuloRepository.findById(id);

        if (!(result == null)) {
            articuloRepository.delete(result);
        }
    }
}
