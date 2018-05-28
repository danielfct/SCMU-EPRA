<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once './config/database.php';
include_once './objects/alarme.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

$alarm = new Alarme($db);

$data = json_decode(file_get_contents("php://input"));

$updateState = property_exists($data, 'estadoAtual');

if ($updateState) {
  $alarm->estadoAtual = $data->estadoAtual;
  $alarm->tempoAtividade = date("Y-m-d H:i:s");
} else {
  $alarm->intensidade = $data->intensidade;
  $alarm->intervalo = $data->intervalo;
  $alarm->tempoDeToque = $data->tempoDeToque;
}

if($alarm->update($updateState)){
    echo '{';
        echo '"message": "Alarm settings were updated.", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to update alarm settings.", "success": "0"';
    echo '}';
}
?>
