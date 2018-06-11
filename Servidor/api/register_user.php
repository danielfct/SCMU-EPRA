<?php
    set_error_handler(function($errno, $errstr, $errfile, $errline ){
        throw new ErrorException($errstr, $errno, 0, $errfile, $errline);
    });

    // required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    include_once './config/database.php';
    include_once './objects/user.php';

    try {
        $database = new Database();
        $db = $database->getConnection();
        $user = new User($db);

        // get posted data
        $data = json_decode(file_get_contents("php://input"));
        // set user property values
        $user->nome = $data->nome;
        $user->telemovel = $data->telemovel;
        $user->email = $data->email;
        $user->password = $data->password;
     //   $user->admin = $data->admin;
     //   $user->privilegios = $data->privilegios;
        $user->pin = $data->pin;
        
        // create the user
        $stmt = $user->create();
        $error = $stmt->errorInfo();
        echo '{';
        if ($error[0] === true) {
            echo '"reply": { "message": "User register successfully", "errorCode": "", "errorMessage": "" }';
        } else {
            echo '"reply": { "message": "Unable to register user", '
                    . '"errorCode": "' . $error[1] . '", '
                    . '"errorMessage": "' . $error[2] . '" }';
        }
        echo '}';
    } catch (ErrorException $e) {
        echo '"reply": { "message": "Unable to register user", "errorCode": "", "errorMessage": "" }';
    }
?>