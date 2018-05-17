<?php
// include database and object file
include_once './config/database.php';

// get database connection
$database = new Database();
$conn = $database->getConnection();

$query = "SELECT estadoAtual FROM alarme LIMIT 1";

$stmt = $conn->prepare($query);

// execute query
$stmt->execute();

$row = $stmt->fetch(PDO::FETCH_ASSOC);
    // extract row
    // this will make $row['name'] to
    // just $name only
    $alarmState = $row["estadoAtual"] == 0 ? "off" : "on";
        echo "alarme=".$alarmState." ";


$query = "SELECT nome, estadoAtual FROM simuladores";

$stmt = $conn->prepare($query);

// execute query
$stmt->execute();

while($row = $stmt->fetch(PDO::FETCH_ASSOC)) {;
  $state = $row["estadoAtual"] == 0 ? "off" : "on";
  echo $row["nome"]."=".$state." ";
}
?>
