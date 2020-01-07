/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const FabricCAServices = require('fabric-ca-client');
const { FileSystemWallet, X509WalletMixin } = require('fabric-network');
const fs = require('fs');
const path = require('path');

const ccpPath = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
const ccpJSON = fs.readFileSync(ccpPath, 'utf8');
const ccp = JSON.parse(ccpJSON);

exports.enrollAdminMain=async (certificateAuthoritiesCustom,adminCustom,enrollmentIDCustom,enrollmentSecretCustom) =>{
    try {

        // Create a new CA client for interacting with the CA.
        //1."certificateAuthoritiesCustom":"ca.org1.example.com"
        //2."adminCustom":"admin"//注册id
        //3."enrollmentIDCustom":"admin"//注册类型
        //4."enrollmentSecretCustom":"adminpw"
        console.log("certificateAuthoritiesCustom是:"+certificateAuthoritiesCustom)
        const caInfo = ccp.certificateAuthorities[certificateAuthoritiesCustom];
        const caTLSCACerts = caInfo.tlsCACerts.pem;
        const ca = new FabricCAServices(caInfo.url, { trustedRoots: caTLSCACerts, verify: false }, caInfo.caName);

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = new FileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the admin user.
        const adminExists = await wallet.exists(adminCustom);
        if (adminExists) {
            console.log(`An identity for the admin user${adminCustom} already exists in the wallet`);
            return {
                "code":"20002",
                "result":"false",
                "message":"管理员id已存在"
            }
        }

        // Enroll the admin user, and import the new identity into the wallet.
        const enrollment = await ca.enroll({ enrollmentID: enrollmentIDCustom, enrollmentSecret: enrollmentSecretCustom });
        const identity = X509WalletMixin.createIdentity('Org1MSP', enrollment.certificate, enrollment.key.toBytes());
        await wallet.import(adminCustom, identity);
        console.log(`Successfully enrolled admin user ${enrollmentIDCustom} and imported it into the wallet`);
        return {
            "code":"200",
            "result":"success",
            "certificateAuthoritiesCustom":certificateAuthoritiesCustom,
            "adminCustom":adminCustom,
            "enrollmentIDCustom":enrollmentIDCustom,
            "enrollmentSecretCustom":enrollmentSecretCustom
        }
    } catch (error) {
        console.error(`Failed to enroll admin user "admin": ${error}`);
        return {
            "code":"20001",
            "result":"false",
            "message":error.message
        }
    }
}

