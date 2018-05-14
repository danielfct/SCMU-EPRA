<?php
    // required headers
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    // get database connection
    include_once './config/database.php';

    // instantiate user object
    include_once './objects/user.php';

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
    $user->admin = $data->admin;
    $user->privilegios = $data->privilegios;

    // create the user
    echo '{';
    if ($user->create()) {
        echo '"message": "User register successfully!"';
    }
    else {
        echo '"message": "Unable to register user."';
    }
    echo '}';
?>