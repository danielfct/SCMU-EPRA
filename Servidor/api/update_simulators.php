<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

include_once './objects/simulator.php';

$database = new Database();
$db = $database->getConnection();

$simulator = new Simulator($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$simulator->id = $data->id;
$simulator->nome = $data->nome;
$simulator->estadoAtual = $data->estadoAtual;
$simulator->atuador = $data->atuador;
$simulator->areaId = $data->areaId;

if($simulator->update()){
    echo '{';
        echo '"message": "Simulator updated successfully!", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to update simulator.", "success": "0"';
    echo '}';
}
?>
