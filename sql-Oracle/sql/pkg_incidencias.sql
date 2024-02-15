CREATE OR REPLACE PACKAGE pkg_incidencias AS
    PROCEDURE insertar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------    
    FUNCTION seleccionar_incidencias RETURN CLOB;
-------------------------------------------------------------------
    PROCEDURE actualizar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------
    PROCEDURE borrar_incidencia(p_json_incidencia IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_incidencias_byUsername(p_usuario IN CLOB) RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_incidencias_byId(p_id IN CLOB) RETURN CLOB;
-------------------------------------------------------------------

    
END pkg_incidencias;
/

create or replace PACKAGE BODY pkg_incidencias AS

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
                'id' VALUE to_char(ID),
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

-------------------------------------------------------------------

FUNCTION seleccionar_incidencias_byUsername(p_usuario IN CLOB) RETURN CLOB IS
    v_usuario VARCHAR2(100);
    v_resultado CLOB;

BEGIN
    v_usuario := JSON_VALUE(p_usuario, '$.usuario');
    SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'id' VALUE to_char(ID),
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
    FROM INCIDENCIAS
    WHERE USUARIO = v_usuario;

    RETURN v_resultado;
END;
-------------------------------------------------------------------
FUNCTION seleccionar_incidencias_byId(p_id IN CLOB) RETURN CLOB IS
    v_id VARCHAR2(100);
    v_resultado CLOB;

BEGIN
    v_id := JSON_VALUE(p_id, '$.id');
    SELECT JSON_ARRAYAGG(
        JSON_OBJECT(
            'id' VALUE to_char(ID),
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
    FROM INCIDENCIAS
    WHERE to_char(ID) = v_id;

    RETURN v_resultado;
END;
-------------------------------------------------------------------

END pkg_incidencias;
/
