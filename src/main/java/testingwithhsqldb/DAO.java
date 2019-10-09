package testingwithhsqldb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

public class DAO {
	private final DataSource myDataSource;
	
	public DAO(DataSource dataSource) {
		myDataSource = dataSource;
	}

	/**
	 * Renvoie le nom d'un client à partir de son ID
	 * @param id la clé du client à chercher
	 * @return le nom du client (LastName) ou null si pas trouvé
	 * @throws SQLException 
	 */
	public String nameOfCustomer(int id) throws SQLException {
		String result = null;
		
		String sql = "SELECT LastName FROM Customer WHERE ID = ?";
		try (Connection myConnection = myDataSource.getConnection(); 
		     PreparedStatement statement = myConnection.prepareStatement(sql)) {
			statement.setInt(1, id); // On fixe le 1° paramètre de la requête
			try ( ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					// est-ce qu'il y a un résultat ? (pas besoin de "while", 
                                        // il y a au plus un enregistrement)
					// On récupère les champs de l'enregistrement courant
					result = resultSet.getString("LastName");
				}
			}
		}
		// dernière ligne : on renvoie le résultat
		return result;
	}
        
        
        
        
        /**
         * Enregistre un nouveau champ de valeurs dans la table PRODUCT
         * 
         * @param id identifiant du produit à ajouter
         * @param name string indiquant le nom du produit
         * @param price prix du produit à ajouter
         * @throws SQLException
         */
        public int AjouterArticle(int id, String name, float price) 
                        throws SQLException{
            
            // Une requête SQL paramétrée
		String sql = "INSERTINTO PRODUCT VALUES(?,?,?)";
		try (Connection connection = myDataSource.getConnection();
			PreparedStatement stmt = connection.prepareStatement(sql))
                {
			// Définir la valeur du paramètre
			stmt.setInt(1, id);
                        stmt.setString(2, name);
                        stmt.setFloat(3, price);
                        
			return stmt.executeUpdate();

		} catch (SQLException ex) {
			throw new SQLException(ex.getMessage());
		}
        }
        
        
        
        /**
         * Recherche un produit dans la table PRODUCT à partir de son 
         * identifiant
         * 
         * @param id integer servant de clé primaire au produit
         * @return product ProductEntity contenant toutes les informations sur 
         *                 le produit contenues dans la table
         * @throws SQLException
         */
	public ProductEntity searchproduct(int id) throws SQLException{
            
            //entité qui contiendra toutes les valeurs sur le produit
            ProductEntity product;
            
            // Une requête SQL paramétrée
            String sql = "SELECT * FROM PRODUCT "
                            + "WHERE ID = ?";
                
            // connexion à la BD puis execution de la requete    
            try (Connection connection = myDataSource.getConnection();
		PreparedStatement stmt = connection.prepareStatement(sql)) {
                
		// Définir la valeur du paramètre
		stmt.setInt(1, id);

                // récupération de la valeur renvoyée
                ResultSet rs = stmt.executeQuery();

                rs.next();
                
                product = new ProductEntity(rs.getInt("ID"),
                                                rs.getString("NAME"),
                                                    rs.getFloat("PRICE"));
                
            } catch (SQLException ex) {
		throw new SQLException(ex.getMessage());
            }
            return product;
        }
        
        
        
        
}
