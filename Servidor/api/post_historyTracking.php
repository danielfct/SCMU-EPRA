<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// get database connection
include_once './config/database.php';

include_once './objects/historyTracking.php';

$database = new Database();
$db = $database->getConnection();

$historyTracking = new HistoryTracking($db);

// get posted data
$data = json_decode(file_get_contents("php://input"));

$historyTracking->area = $data->area;
$historyTracking->duracao = $data->duracao;
$historyTracking->trackingId = $data->trackingId;

if($historyTracking->create()){
    echo '{';
        echo '"message": "HistoryTracking saved successfully!", "success": "1"';
    echo '}';
}

else{
    echo '{';
        echo '"message": "Unable to save HistoryTracking.", "success": "0"';
    echo '}';
}
?>
