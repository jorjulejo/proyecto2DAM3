
CREATE OR REPLACE PACKAGE pkg_usuarios AS
    PROCEDURE insertar_usuario(p_json_usuario IN CLOB);
-------------------------------------------------------------------
    PROCEDURE actualizar_usuario(p_json_usuario IN CLOB);
    -------------------------------------------------------------------
    PROCEDURE borrar_usuario(p_json_usuario IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_usuarios RETURN CLOB;
END pkg_usuarios;
/

CREATE OR REPLACE PACKAGE BODY pkg_usuarios AS

    PROCEDURE insertar_usuario(p_json_usuario IN CLOB) IS
    BEGIN
        INSERT INTO USUARIOS(EMAIL, CONTRASENA, TOKEN, SN_ADMIN)
        VALUES (
            JSON_VALUE(p_json_usuario, '$.email'),
            JSON_VALUE(p_json_usuario, '$.contrasena'),
            JSON_VALUE(p_json_usuario, '$.token'),
            JSON_VALUE(p_json_usuario, '$.sn_admin')
        );
    END;
-------------------------------------------------------------------
    PROCEDURE actualizar_usuario(p_json_usuario IN CLOB) IS
    BEGIN
        UPDATE USUARIOS SET
            CONTRASENA = JSON_VALUE(p_json_usuario, '$.contrasena'),
            TOKEN = JSON_VALUE(p_json_usuario, '$.token'),
            SN_ADMIN = JSON_VALUE(p_json_usuario, '$.sn_admin')
        WHERE EMAIL = JSON_VALUE(p_json_usuario, '$.email');
    END;
-------------------------------------------------------------------
    PROCEDURE borrar_usuario(p_json_usuario IN CLOB) IS
    BEGIN
        DELETE FROM USUARIOS
        WHERE EMAIL = JSON_VALUE(p_json_usuario, '$.email');
    END;
-------------------------------------------------------------------
    FUNCTION seleccionar_usuarios RETURN CLOB IS
        v_resultado CLOB;
    BEGIN
        SELECT JSON_ARRAYAGG(
            JSON_OBJECT(
                'email' VALUE EMAIL,
                'contrasena' VALUE CONTRASENA,
                'token' VALUE TOKEN,
                'sn_admin' VALUE SN_ADMIN
            )
        ) INTO v_resultado
        FROM USUARIOS;
        
        RETURN v_resultado;
    END;
-------------------------------------------------------------------

END pkg_usuarios;
/

