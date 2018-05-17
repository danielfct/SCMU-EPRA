<?php
class User {

    // database connection and table name
    private $conn;
    private $table_name;

    // object properties
    public $nome;
    public $telemovel;
    public $email;
    public $password;
    public $admin;
    public $privilegios;

    // constructor with $db as database connection
    public function __construct($db) {
        $this->conn = $db;
        $this->table_name = "utilizador";
    }

    // read users
    function read($search) {
        // select all query
        if ($search) {
          $query = "SELECT * FROM " . $this->table_name . " WHERE nome LIKE '%$search%'";
        } else if ($this->email) {
          $query = "SELECT * FROM utilizador WHERE email='$email' LIMIT 1";
        } else {
          $query = "SELECT * FROM " . $this->table_name;
        }

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // execute query
        $stmt->execute();

        return $stmt;
    }

    // create user
    function create() {

        // query to insert record
        $query = "INSERT INTO "
                  . $this->table_name . "
                  SET
                    nome=:nome,
                    telemovel=:telemovel,
                    email=:email,
                    password=:password";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->nome=htmlspecialchars(strip_tags($this->nome));
        $this->telemovel=htmlspecialchars(strip_tags($this->telemovel));
        $this->email=htmlspecialchars(strip_tags($this->email));
        $this->password=htmlspecialchars(strip_tags($this->password));

        // bind values
        $stmt->bindParam(":nome", $this->nome);
        $stmt->bindParam(":telemovel", $this->telemovel);
        $stmt->bindParam(":email", $this->email);
        $stmt->bindParam(":password", $this->password);

        // execute query
        $stmt->execute();
    }

    function delete(){

        // delete query
        $query = "DELETE FROM " . $this->table_name . " WHERE email = ?";

        // prepare query
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->email=htmlspecialchars(strip_tags($this->email));

        // bind id of record to delete
        $stmt->bindParam(1, $this->email);

        try {
          // execute query
          if($stmt->execute()){
              return true;
          }
        } catch(PDOException $e) {
        }

        return false;

    }

    function addComma($res) {
      if(strlen($res) > 0) {
        $res .= ", ";
      }
      return $res;
    }

    function buildQueryAttributes() {
      $res = "";
      if($this->privilegios) {
        $res .= "privilegios = :privilegios";
      }
      if($this->nome) {
        $res = $this->addComma($res);
        $res .= "nome = :nome";
      }
      if($this->telemovel) {
        $res = $this->addComma($res);
        $res .= "telemovel = :telemovel";
      }
      return $res;
    }

    function update(){

      $query = "UPDATE
                  " . $this->table_name . "
              SET " . $this->buildQueryAttributes() ." WHERE email = :email";

        // prepare query statement
        $stmt = $this->conn->prepare($query);

        // sanitize
        $this->email=htmlspecialchars(strip_tags($this->email));
        if($this->privilegios) {
          $this->privilegios=htmlspecialchars(strip_tags($this->privilegios));
        }
        if($this->nome) {
          $this->nome=htmlspecialchars(strip_tags($this->nome));
        }
        if($this->telemovel) {
          $this->telemovel=htmlspecialchars(strip_tags($this->telemovel));
        }

        // bind new values
        $stmt->bindParam(':email', $this->email);
        if($this->privilegios) {
          $stmt->bindParam(':privilegios', $this->privilegios);
        }
        if($this->nome) {
          $stmt->bindParam(':nome', $this->nome);
        }
        if($this->telemovel) {
          $stmt->bindParam(':telemovel', $this->telemovel);
        }

        // execute the query

          try {
            if($stmt->execute()){
                return true;
            }
          } catch(PDOException $e){
          }


        return false;
    }
}
