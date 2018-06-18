<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once './config/database.php';
include_once './objects/user.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

$user = new User($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$user->privilegios = $data->privilegios;
$user->email = $data->email;

if ($user->update()) {
    echo '{';
        echo '"message": "User info was updated.", "success": "1"';
    echo '}';
}

else {
    echo '{';
        echo '"message": "Unable to update user info.", "success": "0"';
    echo '}';
}
?>