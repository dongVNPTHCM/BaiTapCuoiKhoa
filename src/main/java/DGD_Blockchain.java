import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.lang.String;

public class DGD_Blockchain {

    public static ArrayList<VNPT_Dong> blockchain = new ArrayList<VNPT_Dong>();
    public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

    public static int difficulty = 3;
    public static float minimumTransaction = 1f;
    public static Store Store1;
    public static Store Store2;
    public static Transaction genesisTransaction;

    public static void main(String[] args) {
        int tam = 1;
        int so = 0;
        int tam2 = 1;
        int tam3 = 1;
        String Block_name = "Block0";
        String Block_name1 = "";
        Scanner BlockData = new Scanner(System.in);
        //add our blocks to the blockchain ArrayList:
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); //Thiết lập bảo mật bằng phương thức BouncyCastleProvider

        //tạo cửa hàng
        Store1 = new Store();
        Store2 = new Store();
        Store coinbase = new Store();

            //NHập số lượng đt vào Store 1
            while (tam3 == 1) {
                System.out.println("HÃY NHẬP SỐ LƯỢNG ĐIỆN THOẠI VÀO STORE 1");
                int i = Integer.parseInt(BlockData.nextLine());
                if (i <= 0) {
                    System.out.println("Thao tác không hợp lệ số lượng điện thoại phải lớn hơn 0!");
                } else {
                    genesisTransaction = new Transaction(coinbase.publicKey, Store1.publicKey, i, null);
                    genesisTransaction.generateSignature(coinbase.privateKey);     //Gán private key (ký thủ công) vào giao dịch gốc
                    genesisTransaction.transactionId = "0"; //Gán ID cho giao dịch gốc
                    genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.reciepient, genesisTransaction.value, genesisTransaction.transactionId)); //Thêm Transactions Output
                    UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); //Lưu giao dịch đầu tiên vào danh sách UTXOs.
                    System.out.println("BLOCK: " + Block_name);
                    System.out.println("Đang tạo và đào khối .... ");
                    VNPT_Dong Block_Name = new VNPT_Dong("0");
                    Block_Name.addTransaction(genesisTransaction);
                    addBlock(Block_Name);
                    System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                    System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                    isChainValid();
                    System.out.println("################################################################");
                    so++;
                    //NHập số lượng đt vào Store 2
                    while (tam2 == 1) {
                        System.out.println("HÃY NHẬP SỐ LƯỢNG ĐIỆN THOẠI VÀO STORE 2");
                        int y = Integer.parseInt(BlockData.nextLine());
                        if (y <= 0) {
                            System.out.println("Thao tác không hợp lệ số lượng điện thoại phải lớn hơn 0!");
                        } else {
                            Transaction transaction2 = new Transaction(coinbase.publicKey, Store2.publicKey, y, null);
                            transaction2.generateSignature(coinbase.privateKey);     //Gán private key (ký thủ công) vào giao dịch gốc
                            transaction2.transactionId = "0";
                            transaction2.outputs.add(new TransactionOutput(transaction2.reciepient, transaction2.value, transaction2.transactionId)); //Thêm Transactions Output
                            UTXOs.put(transaction2.outputs.get(0).id, transaction2.outputs.get(0)); //Lưu giao dịch đầu tiên vào danh sách UTXOs.
                            System.out.println("BLOCK: " + Integer.toString(so));
                            System.out.println("Đang tạo và đào khối.... ");
                            VNPT_Dong Block_Name1 = new VNPT_Dong(Block_Name.hash);
                            Block_Name.addTransaction(transaction2);
                            addBlock(Block_Name1);
                            System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                            System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                            isChainValid();
                            System.out.println("################################################################");
                            tam2 = 0;
                            so++;
                            Block_Name = Block_Name1;
                        }
                    }
                    while (tam == 1) {
                        System.out.println("################################################################");
                        System.out.println("HÃY CHỌN THAO TÁC CẦN LÀM");
                        System.out.println("Ấn 1 nếu muốn chuyển điện thoại từ Store 1 vào Store 2");
                        System.out.println("Ấn 2 nếu muốn chuyển điện thoại từ Store 2 vào Store 1");
                        System.out.println("Ấn 3 để thoát chương trình");
                        System.out.println("################################################################");

                        int z = Integer.parseInt(BlockData.nextLine());
                        if (z > 3) {
                            System.out.println("################################################################");
                            System.out.println("Thao tác không hợp lệ!");
                        } else {
                            if (z == 1) {   //chuyển điện thoại từ store 1 sang store 2
                                System.out.println("Hãy nhập số lượng điện thoại chuyển từ store 1 sang store 2");
                                int DT = Integer.parseInt(BlockData.nextLine());
                                if (DT == 0) {
                                    System.out.println("Số lượng điện thoại chuyển kho phải lớn hơn 0!");
                                } else {
                                    if (DT > Store1.getBalance()) {  //Trường hợp nhập số lượng điện thoại nhiều hơn trong kho
                                        System.out.println("Số lượng điên thoại cần chuyển nhiều hơn trong store!");
                                        //Block_name1 = "Block" + Integer.toString(so);
                                        System.out.println("BLOCK " + Integer.toString(so));
                                        VNPT_Dong Block_Name1 = new VNPT_Dong(Block_Name.hash);
                                        Block_Name1.addTransaction(Store1.sendFunds(Store2.publicKey, DT));
                                        addBlock(Block_Name1);
                                        System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                                        System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                                        isChainValid();
                                        System.out.println("################################################################");
                                        System.out.println("                                                                      ");
                                        System.out.println("                                                                      ");
                                        Block_Name = Block_Name1;
                                        so++;
                                    }
                                    else { //Trường hợp nhập hợp lệ tiến hành chuyển đt từ store 1 sang store 2
                                        System.out.println("Số lượng điên thoại hợp lệ tiến hành chuyển đt từ store 1 sang store 2");
                                        System.out.println("BLOCK " + Integer.toString(so));
                                        VNPT_Dong Block_Name1 = new VNPT_Dong(Block_Name.hash);
                                        Block_Name1.addTransaction(Store1.sendFunds(Store2.publicKey, DT));
                                        addBlock(Block_Name1);
                                        System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                                        System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                                        isChainValid();
                                        System.out.println("################################################################");
                                        System.out.println("                                                                      ");
                                        System.out.println("                                                                      ");
                                        Block_Name = Block_Name1;
                                        so++;
                                    }


                                }
                            }

                            if (z == 2) { //chuyển điện thoại từ store 2 sang store 1
                                System.out.println("Hãy nhập số lượng điện thoại chuyển từ store 2 sang store 1");
                                int DT = Integer.parseInt(BlockData.nextLine());
                                if (DT == 0) {
                                    System.out.println("Số lượng điện thoại chuyển kho phải lớn hơn 0!");
                                } else {
                                    if (DT > Store2.getBalance()) {  //Trường hợp nhập số lượng điện thoại nhiều hơn trong kho
                                        System.out.println("Số lượng điên thoại cần chuyển nhiều hơn trong store!");
                                        System.out.println("BLOCK " + Integer.toString(so));
                                        VNPT_Dong Block_Name1 = new VNPT_Dong(Block_Name.hash);
                                        Block_Name1.addTransaction(Store2.sendFunds(Store1.publicKey, DT));
                                        addBlock(Block_Name1);
                                        System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                                        System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                                        System.out.println("################################################################");
                                        System.out.println("                                                                      ");
                                        System.out.println("                                                                      ");
                                        Block_Name = Block_Name1;
                                        so++;
                                    }
                                    else { //Trường hợp nhập hợp lệ tiến hành chuyển đt từ store 2 sang store 1
                                        System.out.println("Số lượng điên thoại hợp lệ tiến hành chuyển đt từ store 2 sang store 1");
                                        System.out.println("BLOCK " + Integer.toString(so));
                                        VNPT_Dong Block_Name1 = new VNPT_Dong(Block_Name.hash);
                                        Block_Name1.addTransaction(Store2.sendFunds(Store1.publicKey, DT));
                                        addBlock(Block_Name1);
                                        System.out.println("Số lượng điện thoại trong Store 1: " + Store1.getBalance());
                                        System.out.println("Số lượng điện thoại trong Store 2: " + Store2.getBalance());
                                        isChainValid();
                                        System.out.println("################################################################");
                                        System.out.println("                                                                      ");
                                        System.out.println("                                                                      ");
                                        Block_Name = Block_Name1;
                                        so++;
                                    }

                                }

                            }


                            if (z == 3) { //thoat chuong trinh
                                System.out.println("BẠN ĐÃ CHỌN THOÁT CHƯƠNG TRÌNH! TẠM BIỆT VÀ HẸN GẶP LẠI!");
                                tam = 0;
                                tam3 = 0;
                            }

                        }


                    }

                }

    }
    }
    public static Boolean isChainValid() {
        VNPT_Dong currentBlock;
        VNPT_Dong previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        HashMap<String,TransactionOutput> tempUTXOs = new HashMap<String,TransactionOutput>(); //Tạo một danh sách hoạt động tạm thời của các giao dịch chưa được thực thi tại một trạng thái khối nhất định.
        tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {

            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //Kiểm tra, so sánh mã băm đã đăng ký với mã băm được tính toán
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("#Mã băm khối hiện tại không khớp");
                return false;
            }
            //So sánh mã băm của khối trước với mã băm của khối trước đã được đăng ký
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("#Mã băm khối trước không khớp");
                return false;
            }
            //Kiểm tra xem mã băm có lỗi không
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("#Khối này không đào được do lỗi!");
                return false;
            }

            //Vòng lặp kiểm tra các giao dịch
            TransactionOutput tempOutput;
            for(int t=0; t <currentBlock.transactions.size(); t++) {
                Transaction currentTransaction = currentBlock.transactions.get(t);

                if(!currentTransaction.verifySignature()) {
                    System.out.println("#Chữ ký số của giao dịch (" + t + ") không hợp lệ");
                    return false;
                }
                if(currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
                    System.out.println("#Các đầu vào không khớp với đầu ra trong giao dịch (" + t + ")");
                    return false;
                }

                for(TransactionInput input: currentTransaction.inputs) {
                    tempOutput = tempUTXOs.get(input.transactionOutputId);

                    if(tempOutput == null) {
                        System.out.println("#Các đầu vào tham chiếu trong giao dịch (" + t + ") bị thiếu!");
                        return false;
                    }

                    if(input.UTXO.value != tempOutput.value) {
                        System.out.println("#Các đầu vào tham chiếu trong giao dịch (" + t + ") có giá trị không hợp lệ");
                        return false;
                    }

                    tempUTXOs.remove(input.transactionOutputId);
                }

                for(TransactionOutput output: currentTransaction.outputs) {
                    tempUTXOs.put(output.id, output);
                }

                if( currentTransaction.outputs.get(0).reciepient != currentTransaction.reciepient) {
                    System.out.println("#Giao dịch(" + t + ") có người nhận không đúng!");
                    return false;
                }
                if( currentTransaction.outputs.get(1).reciepient != currentTransaction.sender) {
                    System.out.println("#Đầu ra của giao (" + t + ") không đúng với người gửi.");
                    return false;
                }

            }

        }
        System.out.println("Chuỗi khối hợp lệ!");
        return true;
    }

    public static void addBlock(VNPT_Dong newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}
