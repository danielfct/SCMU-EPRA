<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");

include_once './config/database.php';
include_once './objects/device.php';

$database = new Database();
$db = $database->getConnection();
$device = new Device($db);

$stmt = $device->read();
$num = $stmt->rowCount();

if ($num > 0) {
    $device_arr = array();
    while ($row = $stmt->fetch(PDO::FETCH_ASSOC)){
        extract($row);
        $device_item = array(
            "nome" => $nome,
            "tipo" => $tipo,
            "ligado" => $ligado
        );
        array_push($device_arr, $device_item);
    }

    echo json_encode($device_arr);
}

else {
    http_response_code(404);
    echo json_encode(
        array("message" => "No devices found.")
    );
}

