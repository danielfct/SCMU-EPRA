<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once './config/database.php';
include_once './objects/simulator.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

$simulator = new Simulator($db);

$data = json_decode(file_get_contents("php://input"));

  $simulator->nome = $data->nome;
  $simulator->estadoAtual = $data->estadoAtual;
  $simulator->atuador = $data->atuador;
  $simulator->areaId = $data->areaId;

if($simulator->create()){
    echo '{';
        echo '"message": "Simulator created!", "success": "1"';
    echo '}';
}

else{
    echo '{';
        echo '"message": "Unable to create simulator.", "success": "0"';
    echo '}';
}
?>
