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
public class IncidenciaServicio {

	@Autowired
	private EntityManager entityManager;

	public JsonArray seleccionarIncidencias() {
		Query query = entityManager.createNativeQuery("SELECT pkg_incidencias.seleccionar_incidencias() FROM DUAL");
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
	public void insertarIncidencia(String jsonIncidencia) {
		Query query = entityManager.createNativeQuery("CALL pkg_incidencias.insertar_incidencia(:jsonIncidencia)");
		query.setParameter("jsonIncidencia", jsonIncidencia);
		query.executeUpdate();
	}

	@Transactional
	public void actualizarIncidencia(String jsonIncidencia) {
		Query query = entityManager.createNativeQuery("CALL pkg_incidencias.actualizar_incidencia(:jsonIncidencia)");
		query.setParameter("jsonIncidencia", jsonIncidencia);
		query.executeUpdate();
	}

	@Transactional
	public void borrarIncidencia(String jsonIncidencia) {
		Query query = entityManager.createNativeQuery("CALL pkg_incidencias.borrar_incidencia(:jsonIncidencia)");
		query.setParameter("jsonIncidencia", jsonIncidencia);
		query.executeUpdate();
	}
}
