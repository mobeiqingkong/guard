/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { FileSystemWallet, Gateway, X509WalletMixin } = require('fabric-network');
const path = require('path');

const ccpPath = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
exports.registerMain=async (userCustom,adminCustom,affiliationCustom,enrollmentIDCustom,roleCustom)=>{
    try {

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = new FileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);

        // Check to see if we've already enrolled the user.
        const userExists = await wallet.exists(userCustom);
        if (userExists) {
            console.log('An identity for the user ${userCustom} already exists in the wallet');
            return {
                "code":"20012",
                "result":"false",
                "message":"用户id已存在"
            }
        }

        // Check to see if we've already enrolled the admin user.
        const adminExists = await wallet.exists(adminCustom);
        if (!adminExists) {
            console.log('An identity for the admin user ${adminCustom} does not exist in the wallet');
            console.log('Run the enrollAdmin.js application before retrying');
            return {
                "code":"20013",
                "result":"false",
                "message":"管理员id不存在"
            }
        }

        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();
        await gateway.connect(ccpPath, { wallet, identity: adminCustom, discovery: { enabled: true, asLocalhost: true } });

        // Get the CA client object from the gateway for interacting with the CA.
        const ca = gateway.getClient().getCertificateAuthority();
        const adminIdentity = gateway.getCurrentIdentity();

        // Register the user, enroll the user, and import the new identity into the wallet.
        const secret = await ca.register({ affiliation: affiliationCustom, enrollmentID: enrollmentIDCustom, role:roleCustom}, adminIdentity);
        const enrollment = await ca.enroll({ enrollmentID: enrollmentIDCustom, enrollmentSecret: secret });
        const userIdentity = X509WalletMixin.createIdentity('Org1MSP', enrollment.certificate, enrollment.key.toBytes());
        await wallet.import(userCustom, userIdentity);
        console.log('Successfully registered and enrolled admin user ${userCustom} and imported it into the wallet');
        return {
            "code":"200",
            "result":"success",
            "userCustom":userCustom,
            "adminCustom":adminCustom,
            "affiliationCustom":affiliationCustom,
            "enrollmentIDCustom":enrollmentIDCustom,
            "roleCustom":roleCustom
        }
    } catch (error) {
        console.error(`Failed to register user ${userCustom}: ${error}`);
        return {
            "code":"20011",
            "result":"false",
            "message":error.message
        }
    }
}

