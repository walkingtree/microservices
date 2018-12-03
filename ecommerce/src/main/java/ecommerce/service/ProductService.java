/**
 * 
 */
package ecommerce.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import ecommerce.model.Product;

/**
 * @author Ravindar
 *
 */
@Service
public class ProductService {

	static Connection conn;

	private final static String TABLE_NAME = "product";

	private void getDBConfig() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "empower");

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@SuppressWarnings("unchecked")
	public JSONObject getProductInfo(String id) {
		JSONObject result = new JSONObject();
		try {
			getDBConfig();
			String sql = "SELECT * FROM " + TABLE_NAME + " WHERE PRODUCT_ID = " + id;

			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				result.put("productId", rs.getString(1));
				result.put("productName", rs.getString(2));
				result.put("description", rs.getString(3));
				result.put("longDescription", rs.getString(4));
				result.put("partNumber", rs.getString(5));
				result.put("sTDPartNumber", rs.getString(6));
				result.put("quantiryIncrementOrder", rs.getBigDecimal(7));
				result.put("maximumOrderQuantity", rs.getBigDecimal(8));
			}

		} catch (Exception ex) {
			result.put("status", "Failed to get result");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject updateProduct(Product product) {
		JSONObject result = new JSONObject();
		try {
			getDBConfig();
			String query = "update " + TABLE_NAME
							+ "  set PRODUCT_NAME = ?, DESCRIPTION=?, LONG_DESCRIPTION=?, PART_NUMBER =?, STD_PART_NUMBER=?, QUANTITY_INCREMENT_ORDER =? , MAXIMUM_ORDER_QUANTITY=? where PRODUCT_ID = ?";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, product.getProductName());
			preparedStmt.setString(2, product.getDescription());
			preparedStmt.setString(3, product.getLongDescription());
			preparedStmt.setString(4, product.getPartNumber());
			preparedStmt.setString(5, product.getsTDPartNumber());
			preparedStmt.setBigDecimal(6, product.getQuantiryIncrementOrder());
			preparedStmt.setBigDecimal(7, product.getMaximumOrderQuantity());
			preparedStmt.setString(8, product.getProductId());

			preparedStmt.executeUpdate();
			System.out.println("Data updated!");
			result.put("status", "Updated");
		} catch (Exception ex) {
			result.put("status", "Failed to update");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject deleteProduct(String id) {
		JSONObject result = new JSONObject();
		try {
			getDBConfig();
			String query = "delete from " + TABLE_NAME + " WHERE PRODUCT_ID = " + id;
			PreparedStatement preparedStmt = conn.prepareStatement(query);

			preparedStmt.execute();
			result.put("status", "Deleted");

		} catch (Exception ex) {
			result.put("status", "Failed to delete");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public JSONObject addProduct(Product product) {
		JSONObject result = new JSONObject();
		try {
			getDBConfig();
			String query = "INSERT INTO " + TABLE_NAME
							+ "(PRODUCT_NAME, DESCRIPTION, LONG_DESCRIPTION, PART_NUMBER, STD_PART_NUMBER, QUANTITY_INCREMENT_ORDER, MAXIMUM_ORDER_QUANTITY, PRODUCT_ID) Values (?,?,?,?,?,?,?,?)";
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString(1, product.getProductName());
			preparedStmt.setString(2, product.getDescription());
			preparedStmt.setString(3, product.getLongDescription());
			preparedStmt.setString(4, product.getPartNumber());
			preparedStmt.setString(5, product.getsTDPartNumber());
			preparedStmt.setBigDecimal(6, product.getQuantiryIncrementOrder());
			preparedStmt.setBigDecimal(7, product.getMaximumOrderQuantity());
			preparedStmt.setString(8, product.getProductId());

			preparedStmt.execute();
			result.put("status", "Created");

		} catch (Exception ex) {
			result.put("status", "Failed to delete");
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

}
