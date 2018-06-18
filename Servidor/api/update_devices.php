<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

include_once './objects/device.php';

$database = new Database();
$db = $database->getConnection();

$device = new Device($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$check = false;
if (property_exists($data, 'nome')) {
  $device->nome = $data->nome;
  $check = true;
}
if (property_exists($data, 'tipo')) {
  $device->tipo = $data->tipo;
}
if (property_exists($data, 'ligado')) {
  $device->ligado = $data->ligado;
}
if (property_exists($data, 'areaId')) {
  $device->areaId = $data->areaId;
}

if($check && $device->update()) {
    echo '{';
        echo '"message": "Device updated successfully!", "success": "1"';
    echo '}';
}

else {
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to update device.", "success": "0"';
    echo '}';
}
?>
