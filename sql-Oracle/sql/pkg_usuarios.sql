create or replace PACKAGE pkg_usuarios AS
    PROCEDURE insertar_usuario(p_json_usuario IN CLOB);
-------------------------------------------------------------------
    PROCEDURE actualizar_usuario(p_json_usuario IN CLOB);
    -------------------------------------------------------------------
    PROCEDURE borrar_usuario(p_json_usuario IN CLOB);
-------------------------------------------------------------------
    FUNCTION seleccionar_usuarios RETURN CLOB;
-------------------------------------------------------------------
    FUNCTION seleccionar_usuario(p_email IN CLOB) RETURN CLOB;
----------------------------------------------------------------------
    FUNCTION seleccionar_admin(p_email IN CLOB) RETURN CLOB;
    
END pkg_usuarios;
/

create or replace PACKAGE BODY pkg_usuarios AS

    PROCEDURE insertar_usuario(p_json_usuario IN CLOB) IS
    BEGIN
        INSERT INTO USUARIOS(EMAIL, CONTRASENA, TOKEN, SN_ADMIN)
        VALUES (
            JSON_VALUE(p_json_usuario, '$.email'),
            JSON_VALUE(p_json_usuario, '$.contrasena'),
            JSON_VALUE(p_json_usuario, '$.token'),
            JSON_VALUE(p_json_usuario, '$.snAdmin'));
    END;
-------------------------------------------------------------------
    PROCEDURE actualizar_usuario(p_json_usuario IN CLOB) IS
    BEGIN
        UPDATE USUARIOS SET
            CONTRASENA = nvl(JSON_VALUE(p_json_usuario, '$.contrasena'),(select contrasena
                                                                            from usuarios
                                                                            where email= JSON_VAlUE(p_json_usuario, '$.email'))),
            TOKEN = NVL(JSON_VALUE(p_json_usuario, '$.token'),(select token
                                                                            from usuarios
                                                                            where email= JSON_VAlUE(p_json_usuario, '$.email'))),
            SN_ADMIN = nvl(JSON_VALUE(p_json_usuario, '$.snAdmin'),(select sn_admin
                                                                            from usuarios
                                                                            where email= JSON_VAlUE(p_json_usuario, '$.email')))
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
                'snAdmin' VALUE SN_ADMIN
            )
        ) INTO v_resultado
        FROM USUARIOS;

        RETURN v_resultado;
    END;
-------------------------------------------------------------------
    FUNCTION seleccionar_usuario(p_email IN CLOB) RETURN CLOB IS
            v_resultado CLOB;
            v_email VARCHAR2(100);
        BEGIN
        v_email := JSON_VALUE(p_email, '$.email');
            SELECT JSON_OBJECT(
                    'email' VALUE EMAIL,
                    'contrasena' VALUE CONTRASENA,
                    'token' VALUE TOKEN,
                    'snAdmin' VALUE SN_ADMIN
                ) INTO v_resultado
            FROM USUARIOS
            WHERE EMAIL = v_email;

            RETURN v_resultado;
        END;
        
 -------------------------------------------------------------------
    FUNCTION seleccionar_admin(p_email IN CLOB) RETURN CLOB IS
            v_resultado CLOB;
            v_email VARCHAR2(100);
        BEGIN
        v_email := JSON_VALUE(p_email, '$.email');
            SELECT JSON_OBJECT(
                    'snAdmin' VALUE SN_ADMIN
                ) INTO v_resultado
            FROM USUARIOS
            WHERE EMAIL = v_email;

            RETURN v_resultado;
        END;
--------------------------------------------------------------------------
END pkg_usuarios;
/