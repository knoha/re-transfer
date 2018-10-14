# re-transfer

### Usage
Database with 2 accounts will be created on app start.  
First account (`id=1`) will have balance of 10000.59 USD, second (`id=2`) - 245.21 USD. 

To get data about account send a **GET** request to `http://localhost:4567/rt/api/bank-accounts/:id`.

To make a transfer between accounts send a **POST** to `http://localhost:4567/rt/api/transfer` with payload:  
```json
{
	"amount": 45.21,
	"senderAccountId": 2,
	"recipientAccountId": 1
}
```

### Installation guide
1. Clone a git repo to a specified folder:  
`git clone https://github.com/knoha/re-transfer.git`

2. In that folder build a project:  
`mvn clean install`

3. Start application by typing:  
`java -jar target/re-transfer-1.0-SNAPSHOT-jar-with-dependencies.jar`