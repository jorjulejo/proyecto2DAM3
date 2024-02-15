CREATE OR REPLACE PACKAGE pkg_camaras AS
    PROCEDURE insertar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    PROCEDURE actualizar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    PROCEDURE borrar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_camaras RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_camaras_byUsuario(p_usuario_json IN CLOB) RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_imagen_byId(p_id_json IN CLOB) RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_camaras_byId(p_id IN CLOB) RETURN CLOB;
-------------------------------------------------------------------
END pkg_camaras;
/

create or replace PACKAGE BODY pkg_camaras AS

    PROCEDURE insertar_camara(p_json_camara IN CLOB) IS
    BEGIN
        INSERT INTO CAMARAS(ID, NOMBRE, URL, LATITUD, LONGITUD, CARRETERA, KILOMETRO, IMAGEN, USUARIO)
        VALUES (
            JSON_VALUE(p_json_camara, '$.id'),
            JSON_VALUE(p_json_camara, '$.nombre'),
            JSON_VALUE(p_json_camara, '$.url'),
            JSON_VALUE(p_json_camara, '$.latitud'),
            JSON_VALUE(p_json_camara, '$.longitud'),
            JSON_VALUE(p_json_camara, '$.carretera'),
            JSON_VALUE(p_json_camara, '$.kilometro'),
            JSON_VALUE(p_json_camara, '$.imagen'),
            JSON_VALUE(p_json_camara, '$.usuario')
        );
    END;
-------------------------------------------------------------------
    PROCEDURE actualizar_camara(p_json_camara IN CLOB) IS
    BEGIN
        UPDATE CAMARAS SET
            NOMBRE = JSON_VALUE(p_json_camara, '$.nombre'),
            URL = JSON_VALUE(p_json_camara, '$.url'),
            LATITUD = JSON_VALUE(p_json_camara, '$.latitud'),
            LONGITUD = JSON_VALUE(p_json_camara, '$.longitud'),
            CARRETERA = JSON_VALUE(p_json_camara, '$.carretera'),
            KILOMETRO = JSON_VALUE(p_json_camara, '$.kilometro'),
            IMAGEN = JSON_VALUE(p_json_camara, '$.imagen'),
            USUARIO = JSON_VALUE(p_json_camara, '$.usuario')
        WHERE ID = JSON_VALUE(p_json_camara, '$.id');
    END;
-------------------------------------------------------------------
    PROCEDURE borrar_camara(p_json_camara IN CLOB) IS
    BEGIN
        DELETE FROM CAMARAS
        WHERE ID = JSON_VALUE(p_json_camara, '$.id');
    END;
-------------------------------------------------------------------
    FUNCTION seleccionar_camaras RETURN CLOB IS
        v_resultado CLOB;
    BEGIN
        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'id' VALUE to_char(ID),
                'nombre' VALUE NOMBRE,
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'carretera' VALUE CARRETERA,
                'kilometro' VALUE KILOMETRO,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM CAMARAS;

        RETURN v_resultado;
    END;
-------------------------------------------------------------------

FUNCTION seleccionar_camaras_byUsuario(p_usuario_json IN CLOB) RETURN CLOB IS
    v_resultado CLOB;
    v_usuario VARCHAR2(100);
BEGIN
    -- Extraer el valor 'usuario' del JSON de entrada
    v_usuario := JSON_VALUE(p_usuario_json, '$.usuario');
    
    SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'id' VALUE to_char(ID),
            'nombre' VALUE NOMBRE,
            'latitud' VALUE LATITUD,
            'longitud' VALUE LONGITUD,
            'carretera' VALUE CARRETERA,
            'kilometro' VALUE KILOMETRO,
            'usuario' VALUE USUARIO
        )
    ) INTO v_resultado
    FROM CAMARAS
    WHERE USUARIO = v_usuario;

    RETURN v_resultado;
END;

-------------------------------------------------------------------
FUNCTION seleccionar_camaras_byId(p_id IN CLOB) RETURN CLOB IS
    v_resultado CLOB;
    v_id VARCHAR2(100);
BEGIN
    -- Extraer el valor 'usuario' del JSON de entrada
    v_id := JSON_VALUE(p_id, '$.id');
    
    SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'id' VALUE to_char(ID),
            'nombre' VALUE NOMBRE,
            'latitud' VALUE LATITUD,
            'longitud' VALUE LONGITUD,
            'carretera' VALUE CARRETERA,
            'kilometro' VALUE KILOMETRO,
            'usuario' VALUE USUARIO
        )
    ) INTO v_resultado
    FROM CAMARAS
    WHERE to_char(id) = v_id;

    RETURN v_resultado;
END;

-------------------------------------------------------------------

FUNCTION seleccionar_imagen_byId(p_id_json IN CLOB) RETURN CLOB IS
    v_resultado CLOB;
    v_id VARCHAR2(100);
BEGIN
    -- Extraer el valor 'id' del JSON de entrada
    v_id := JSON_VALUE(p_id_json, '$.id');
    
    SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'urL' VALUE URL,
            'imagen' VALUE IMAGEN
        )
    ) INTO v_resultado
    FROM CAMARAS
    WHERE ID = v_id;

    RETURN v_resultado;
END;
-------------------------------------------------------------------

END pkg_camaras;
/