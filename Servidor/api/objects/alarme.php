<?php
class Alarme {

    // database connection and table name
    private $conn;
    private $table_name = "alarme";

    // object properties
    public $estadoAtual;
    public $tempoAtividade;
    public $intensidade;
    public $intervalo;
    public $tempoDeToque;

    // constructor with $db as database connection
    public function __construct($db){
        $this->conn = $db;
    }

    // read simulators
    function read(){

        // select all query
        $query = "SELECT *, NOW() AS dataAtual FROM alarme LIMIT 1";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create simulator
    function create(){

        // query to insert record
        $query = "INSERT INTO
                    " . $this->table_name . "
                SET
                    estadoAtual=:estadoAtual, tempoAtividade:tempoAtividade, intensidade:intensidade, intervalo:intervalo, tempoDeToque:tempoDeToque";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->estadoAtual=htmlspecialchars(strip_tags($this->estadoAtual));
        $this->tempoAtividade=htmlspecialchars(strip_tags($this->tempoAtividade));
        $this->intensidade=htmlspecialchars(strip_tags($this->intensidade));
        $this->intervalo=htmlspecialchars(strip_tags($this->intervalo));
        $this->tempoDeToque=htmlspecialchars(strip_tags($this->tempoDeToque));

        // bind values
        $stmt->bindParam(":estadoAtual", $this->estadoAtual);
        $stmt->bindParam(":tempoAtividade", $this->tempoAtividade);
        $stmt->bindParam(":intensidade", $this->intensidade);
        $stmt->bindParam(":intervalo", $this->intervalo);
        $stmt->bindParam(":tempoDeToque", $this->tempoDeToque);

        // execute query
        if($stmt->execute()){
            return true;
        }

        return false;

    }

    // update alarm settings
    function update($updateState){

      if($updateState) {
        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    estadoAtual = :estadoAtual,
                    tempoAtividade = :tempoAtividade
                WHERE
                    1";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->estadoAtual=htmlspecialchars(strip_tags($this->estadoAtual));
        $this->tempoAtividade=htmlspecialchars(strip_tags($this->tempoAtividade));

        // bind new values
        $stmt->bindParam(':estadoAtual', $this->estadoAtual);
        $stmt->bindParam(':tempoAtividade', $this->tempoAtividade);
      } else {
        // update query
        $query = "UPDATE
                    " . $this->table_name . "
                SET
                    intensidade = :intensidade,
                    intervalo = :intervalo,
                    tempoDeToque = :tempoDeToque
                WHERE
                    1";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->intensidade=htmlspecialchars(strip_tags($this->intensidade));
        $this->intervalo=htmlspecialchars(strip_tags($this->intervalo));
        $this->tempoDeToque=htmlspecialchars(strip_tags($this->tempoDeToque));

        // bind new values
        $stmt->bindParam(':intensidade', $this->intensidade);
        $stmt->bindParam(':intervalo', $this->intervalo);
        $stmt->bindParam(':tempoDeToque', $this->tempoDeToque);
      }

        // execute the query
        if($stmt->execute()){
            return true;
        }

        return false;
    }
}
