
CREATE OR REPLACE PACKAGE pkg_flujos AS
    PROCEDURE insertar_flujo(p_json_flujo IN CLOB);
-------------------------------------------------------------------
    PROCEDURE actualizar_flujo(p_json_flujo IN CLOB);
-------------------------------------------------------------------
    PROCEDURE borrar_flujo(p_json_flujo IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_flujos RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_flujos_byUsername (p_username in CLOB) RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_flujos_byId (p_id in CLOB) RETURN CLOB;
-------------------------------------------------------------------

END pkg_flujos;
/

create or replace PACKAGE BODY pkg_flujos AS

    PROCEDURE insertar_flujo(p_json_flujo IN CLOB) IS
    BEGIN
        INSERT INTO FLUJOS(ID, FECHA, RANGO_TIEMPO, MEDIA_VELOCIDAD, TOTAL_VEHICULOS, LATITUD, LONGITUD, USUARIO)
        VALUES (
            JSON_VALUE(p_json_flujo, '$.id'),
            TO_DATE(JSON_VALUE(p_json_flujo, '$.fecha'), 'YYYY-MM-DD'),
            JSON_VALUE(p_json_flujo, '$.rango_tiempo'),
            JSON_VALUE(p_json_flujo, '$.media_velocidad'),
            JSON_VALUE(p_json_flujo, '$.total_vehiculos'),
            JSON_VALUE(p_json_flujo, '$.latitud'),
            JSON_VALUE(p_json_flujo, '$.longitud'),
            JSON_VALUE(p_json_flujo, '$.usuario')
        );
    END;
-------------------------------------------------------------------
    PROCEDURE actualizar_flujo(p_json_flujo IN CLOB) IS
    BEGIN
        UPDATE FLUJOS SET
            FECHA = TO_DATE(JSON_VALUE(p_json_flujo, '$.fecha'), 'YYYY-MM-DD'),
            RANGO_TIEMPO = TO_NUMBER(JSON_VALUE(p_json_flujo, '$.rango_tiempo')),
            MEDIA_VELOCIDAD = TO_NUMBER(JSON_VALUE(p_json_flujo, '$.media_velocidad')),
            TOTAL_VEHICULOS = TO_NUMBER(JSON_VALUE(p_json_flujo, '$.total_vehiculos')),
            LATITUD = JSON_VALUE(p_json_flujo, '$.latitud'),
            LONGITUD = JSON_VALUE(p_json_flujo, '$.longitud'),
            USUARIO = JSON_VALUE(p_json_flujo, '$.usuario')
        WHERE ID = JSON_VALUE(p_json_flujo, '$.id');
    END;
-------------------------------------------------------------------
    PROCEDURE borrar_flujo(p_json_flujo IN CLOB) IS
    BEGIN
        DELETE FROM FLUJOS
        WHERE ID = JSON_VALUE(p_json_flujo, '$.id');
    END;
-------------------------------------------------------------------
    FUNCTION seleccionar_flujos RETURN CLOB IS
        v_resultado CLOB;
    BEGIN
        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'id' VALUE to_char(ID),
                'fecha' VALUE TO_CHAR(FECHA, 'YYYY-MM-DD'),
                'rango_tiempo' VALUE to_char(RANGO_TIEMPO),
                'media_velocidad' VALUE to_char(MEDIA_VELOCIDAD),
                'total_vehiculos' VALUE to_char(TOTAL_VEHICULOS),
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM FLUJOS;

        RETURN v_resultado;
    END;
-------------------------------------------------------------------
FUNCTION seleccionar_flujos_byUsername (p_username in CLOB) RETURN CLOB IS
        v_resultado CLOB;
        v_username VARCHAR2(100);
    BEGIN
        v_username := JSON_VALUE(p_username, '$.usuario');

        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'id' VALUE to_char(ID),
                'fecha' VALUE TO_CHAR(FECHA, 'YYYY-MM-DD'),
                'rango_tiempo' VALUE RANGO_TIEMPO,
                'media_velocidad' VALUE MEDIA_VELOCIDAD,
                'total_vehiculos' VALUE TOTAL_VEHICULOS,
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM FLUJOS
        WHERE USUARIO = v_username;

        RETURN v_resultado;
    END;

-------------------------------------------------------------------
FUNCTION seleccionar_flujos_byId (p_id in CLOB) RETURN CLOB IS
        v_resultado CLOB;
        v_id VARCHAR2(100);
    BEGIN
        v_id := JSON_VALUE(p_id, '$.id');

        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'id' VALUE to_char(ID),
                'fecha' VALUE TO_CHAR(FECHA, 'YYYY-MM-DD'),
                'rango_tiempo' VALUE RANGO_TIEMPO,
                'media_velocidad' VALUE MEDIA_VELOCIDAD,
                'total_vehiculos' VALUE TOTAL_VEHICULOS,
                'latitud' VALUE LATITUD,
                'longitud' VALUE LONGITUD,
                'usuario' VALUE USUARIO
            )
        ) INTO v_resultado
        FROM FLUJOS
        WHERE to_char(id) = v_id;

        RETURN v_resultado;
    END;
END pkg_flujos;
/


