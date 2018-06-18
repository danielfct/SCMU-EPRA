<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

include_once './objects/area.php';

$database = new Database();
$db = $database->getConnection();

$area = new Area($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$area->nome = $data->nome;
$area->alarmeLigado = $data->alarmeLigado;

if($area->create()){
    echo '{';
        echo '"message": "Area saved successfully!", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to save area.", "success": "0"';
    echo '}';
}
?>
