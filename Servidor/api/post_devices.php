<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once './config/database.php';
include_once './objects/device.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

$device = new Device($db);

$data = json_decode(file_get_contents("php://input"));

  $device->nome = $data->nome;
  $device->tipo = $data->tipo;
  $device->ligado = $data->ligado;

if($device->create()){
    echo '{';
        echo '"message": "Device created!", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to create device.", "success": "0"';
    echo '}';
}
?>
