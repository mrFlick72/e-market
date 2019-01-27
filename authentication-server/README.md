
keytool -genkey -alias emarket -keyalg RSA -keypass secret -storepass secret -keystore keystore.jks

keytool -genkeypair -alias emarket -keyalg RSA -keysize 2048 -keystore keystore.jks

keytool -export -alias emarket -keystore keystore.jks -file emarket.pem

openssl x509 -inform der -in emarket.pem -pubkey -noout
