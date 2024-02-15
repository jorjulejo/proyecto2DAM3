package com.authapi.authapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonReader;
import java.io.StringReader;
import java.sql.Clob;
import java.sql.SQLException;

@Service
public class FlujoServicio {

	@Autowired
	private EntityManager entityManager;

	public JsonArray seleccionarFlujos() {
		Query query = entityManager.createNativeQuery("SELECT pkg_Flujos.seleccionar_Flujos() FROM DUAL");
		Clob clob = (Clob) query.getSingleResult();
		String jsonString = convertClobToString(clob);
		JsonArray jsonArray = null;

		try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
			jsonArray = jsonReader.readArray(); // Cambiado de readObject() a readArray()
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArray;
	}
	
	public JsonArray seleccionarFlujosByUsername(String username) {
		Query query = entityManager.createNativeQuery("SELECT pkg_flujos.seleccionar_flujos_byUsername(:username) FROM DUAL");
		query.setParameter("username", username);
		Clob clob = (Clob) query.getSingleResult();
		String jsonString = convertClobToString(clob);
		JsonArray jsonArray = null;

		try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
			jsonArray = jsonReader.readArray(); // Cambiado de readObject() a readArray()
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArray;
	}
	
	public JsonArray seleccionarFlujosbyId(String id) {
		Query query = entityManager.createNativeQuery("SELECT pkg_flujos.seleccionar_flujos_byId(:id) FROM DUAL");
		query.setParameter("id", id);
		Clob clob = (Clob) query.getSingleResult();
		String jsonString = convertClobToString(clob);
		JsonArray jsonArray = null;

		try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
			jsonArray = jsonReader.readArray(); // Cambiado de readObject() a readArray()
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonArray;
	}
	

	private String convertClobToString(Clob clob) {
		if (clob == null) {
	        // Manejar el caso nulo, por ejemplo, devolver una cadena vacía o null
	        return ""; // o puedes devolver "" si prefieres una cadena vacía
	    }
		StringBuilder sb = new StringBuilder();
		try {
			java.io.Reader reader = clob.getCharacterStream();
			java.io.BufferedReader br = new java.io.BufferedReader(reader);

			String line;
			while (null != (line = br.readLine())) {
				sb.append(line);
			}
			br.close();
		} catch (SQLException | java.io.IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Transactional
	public void insertarFlujo(String jsonFlujo) {
		Query query = entityManager.createNativeQuery("CALL pkg_Flujos.insertar_Flujo(:jsonFlujo)");
		query.setParameter("jsonFlujo", jsonFlujo);
		query.executeUpdate();
	}

	@Transactional
	public void actualizarFlujo(String jsonFlujo) {
		Query query = entityManager.createNativeQuery("CALL pkg_Flujos.actualizar_Flujo(:jsonFlujo)");
		query.setParameter("jsonFlujo", jsonFlujo);
		query.executeUpdate();
	}

	@Transactional
	public void borrarFlujo(String jsonFlujo) {
		Query query = entityManager.createNativeQuery("CALL pkg_Flujos.borrar_Flujo(:jsonFlujo)");
		query.setParameter("jsonFlujo", jsonFlujo);
		query.executeUpdate();
	}
}
