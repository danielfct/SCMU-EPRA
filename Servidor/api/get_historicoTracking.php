<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Headers: access");
header("Access-Control-Allow-Methods: GET");
header("Access-Control-Allow-Credentials: true");
header('Content-Type: application/json');

include_once './config/database.php';
include_once './objects/historyTracking.php';

$database = new Database();
$db = $database->getConnection();
$history = new HistoryTracking($db);

$stmt = $history->read(isset($_GET['trackingId']) ? $_GET['trackingId'] : NULL);
$num = $stmt->rowCount();

if ($num > 0){
    $history_arr = array();
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $history_item = array(
            "area" => $nome,
            "duracao" => $duracao,
            "trackingId" => $trackingId,
        );
        array_push($history_arr, $history_item);
    }
    echo json_encode($history_arr);
}

else{
    http_response_code(404);
    echo json_encode(
        array("message" => "No history tracking found.")
    );
}
