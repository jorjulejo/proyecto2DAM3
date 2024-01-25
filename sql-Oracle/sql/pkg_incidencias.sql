CREATE OR REPLACE PACKAGE pkg_incidencias AS
    PROCEDURE insertar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------    
    FUNCTION seleccionar_incidencias RETURN CLOB;
-------------------------------------------------------------------
    PROCEDURE actualizar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------
    PROCEDURE borrar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------
    
END pkg_incidencias;
/

CREATE OR REPLACE PACKAGE BODY pkg_incidencias AS

    PROCEDURE insertar_incidencia(p_json_incidencia IN CLOB) IS
    BEGIN
        INSERT INTO INCIDENCIAS(ID, TIPO, CAUSA, COMIENZO, NVL_INCIDENCIA, CARRETERA, DIRECCION, LATITUD, LONGITUD, USUARIO)
        VALUES (
            JSON_VALUE(p_json_incidencia, '$.id'),
            JSON_VALUE(p_json_incidencia, '$.tipo'),
            JSON_VALUE(p_json_incidencia, '$.causa'),
            TO_DATE(JSON_VALUE(p_json_incidencia, '$.comienzo'), 'YYYY-MM-DD'),
            JSON_VALUE(p_json_incidencia, '$.nivel_incidencia'),
            JSON_VALUE(p_json_incidencia, '$.carretera'),
            JSON_VALUE(p_json_incidencia, '$.direccion'),
            JSON_VALUE(p_json_incidencia, '$.latitud'),
            JSON_VALUE(p_json_incidencia, '$.longitud'),
            JSON_VALUE(p_json_incidencia, '$.usuario')
        );
    END;
-------------------------------------------------------------------
    FUNCTION seleccionar_incidencias RETURN CLOB IS
        v_resultado CLOB;
    BEGIN
        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'id' VALUE ID,
                'tipo' VALUE TIPO,
                'causa' VALUE CAUSA,
                'comienzo' VALUE TO_CHAR(COMIENZO, 'YYYY-MM-DD'),
                'nivel_incidencia' VALUE NVL_INCIDENCIA,
                'carretera' VALUE CARRETERA,
                'direccion' VALUE DIRECCION,
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM INCIDENCIAS;
        
        RETURN v_resultado;
    END;
-------------------------------------------------------------------    
    PROCEDURE actualizar_incidencia(p_json_incidencia IN CLOB) IS
    BEGIN
        UPDATE INCIDENCIAS SET
            TIPO = JSON_VALUE(p_json_incidencia, '$.tipo'),
            CAUSA = JSON_VALUE(p_json_incidencia, '$.causa'),
            COMIENZO = TO_DATE(JSON_VALUE(p_json_incidencia, '$.comienzo'), 'YYYY-MM-DD'),
            NVL_INCIDENCIA = JSON_VALUE(p_json_incidencia, '$.nivel_incidencia'),
            CARRETERA = JSON_VALUE(p_json_incidencia, '$.carretera'),
            DIRECCION = JSON_VALUE(p_json_incidencia, '$.direccion'),
            LATITUD = JSON_VALUE(p_json_incidencia, '$.latitud'),
            LONGITUD = JSON_VALUE(p_json_incidencia, '$.longitud'),
            USUARIO = JSON_VALUE(p_json_incidencia, '$.usuario')
        WHERE ID = JSON_VALUE(p_json_incidencia, '$.id');
    END;
-------------------------------------------------------------------
    PROCEDURE borrar_incidencia(p_json_incidencia IN CLOB) IS
    BEGIN
        DELETE FROM INCIDENCIAS
        WHERE ID = JSON_VALUE(p_json_incidencia, '$.id');
    END;


END pkg_incidencias;
/
