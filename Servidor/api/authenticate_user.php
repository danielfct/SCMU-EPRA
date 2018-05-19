<?php
    set_error_handler(function($errno, $errstr, $errfile, $errline ){
        throw new ErrorException($errstr, $errno, 0, $errfile, $errline);
    });

    // required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: GET");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    include_once './config/database.php';
    include_once './objects/user.php';

    try {
        $database = new Database();
        $db = $database->getConnection();
        $user = new User($db);

        // get data
        $data = json_decode(file_get_contents("php://input"));
        // set user property values
        $user->email = $data->email;
        $user->password = $data->password;

        // get the user
        $stmt = $user->read();
        $error = $stmt->errorInfo();
        $result = $stmt->fetch();
        echo '{';
        if ($error[0] === false) {
            echo '"reply": { "message": "User authentication failed", '
                . '"errorCode": "' . $error[1] . '", '
                . '"errorMessage": "' . $error[2] . '" }';
        } else if (!$result) {
            echo '"reply": { "message": "User authentication failed", '
                . '"errorCode": "1", '
                . '"errorMessage": "Unregistered email" }';
        } else if (strcmp($result['password'], $user->password) != 0) {
            echo '"reply": { "message": "User authentication failed", '
                . '"errorCode": "2", '
                . '"errorMessage": "Incorrect password" }';
        } else {
            echo '"reply": { "message": "User authentication successfully", '
                . '"errorCode": "", '
                . '"errorMessage": "" }';
        }
        echo '}';
    } catch (ErrorException $e) {
        echo $e;
        echo '"reply": { "message": "User authentication failed", "errorCode": "", "errorMessage": "" }';
    }
?>