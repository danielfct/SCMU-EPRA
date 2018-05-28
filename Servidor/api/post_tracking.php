<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once './config/database.php';
include_once './objects/tracking.php';

// get database connection
$database = new Database();
$db = $database->getConnection();

$tracking = new Tracking($db);

$data = json_decode(file_get_contents("php://input"));

  $tracking->areaAtual = $data->areaAtual;
  $tracking->areaEntrada = $data->areaEntrada;
  $tracking->pessoasNotificadas = $data->pessoasNotificadas;

if($tracking->create()){
    echo '{';
        echo '"message": "Tracking created!", "success": "1"';
    echo '}';
}

else{
    http_response_code(404);
    echo '{';
        echo '"message": "Unable to create tracking.", "success": "0"';
    echo '}';
}
?>
