CREATE OR REPLACE PACKAGE pkg_camaras AS
    PROCEDURE insertar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    PROCEDURE actualizar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    PROCEDURE borrar_camara(p_json_camara IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_camaras RETURN CLOB;
-------------------------------------------------------------------
END pkg_camaras;
/

CREATE OR REPLACE PACKAGE BODY pkg_camaras AS

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
            TO_NUMBER(JSON_VALUE(p_json_camara, '$.kilometro')),
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
            KILOMETRO = TO_NUMBER(JSON_VALUE(p_json_camara, '$.kilometro')),
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
                'id' VALUE ID,
                'nombre' VALUE NOMBRE,
                'url' VALUE URL,
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'carretera' VALUE CARRETERA,
                'kilometro' VALUE KILOMETRO,
                'imagen' VALUE IMAGEN,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM CAMARAS;
        
        RETURN v_resultado;
    END;
-------------------------------------------------------------------
END pkg_camaras;
/


