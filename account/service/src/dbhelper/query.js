/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { FileSystemWallet, Gateway } = require('fabric-network');
const path = require('path');

const ccpPathBackup = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
const ccpPath = path.resolve('/root/Project/fabric-samples/first-network', 'connection-org1.json');

exports.queryMain=async (userCustom ,channelCustom,contractCustom,params)=> {
    try {

        // Create a new file system based wallet for managing identities.
        
        const walletPath = path.resolve(__dirname, '..', '..', 'wallet');
        const wallet = new FileSystemWallet(walletPath);
        //const walletPath = path.resolve(__dirname, '..', '..', 'wallet');
        console.log(`Wallet path: ${walletPath}`);
        // Check to see if we've already enrolled the user.
        //1."userCustom":"user1"
        //2."identityCustom":"user1"
        //3."channelCustom":"mychannel"
        //4."contractCustom":"fabcar"
        const userExists = await wallet.exists(userCustom);
        if (!userExists) {
            console.log(`An identity for the user ${userCustom} does not exist in the wallet`);
            console.log(`Run the registerUser.js application before retrying`);
            return{
                "code":"20012",
                "result":"false",
                "message":"用户id不存在"
            };
        }

        // Create a new gateway for connecting to our peer node.
        const gateway = new Gateway();

        await gateway.connect(ccpPath, { wallet, identity:userCustom, discovery: { enabled: true, asLocalhost: true } });
        // Get the network (channel) our contract is deployed to.
        console.log('ccpPath路径是:'+ccpPath)
        console.log('打印到获取通道')
        const network = await gateway.getNetwork(channelCustom);

        // Get the contract from the network.
        console.log('打印到获取合约')
        const contract = network.getContract(contractCustom);

        // Evaluate the specified transaction.
        // queryCar transaction - requires 1 argument, ex: ('queryCar', 'CAR4')
        // queryAllCars transaction - requires no arguments, ex: ('queryAllCars')
        // const searchResult = await contract.evaluateTransaction('queryAllCars');
        var keyAry = [];
        var valueAry = [];
        for(var key in params){
            keyAry.push(key);
            valueAry.push(params[key]);
        }
        console.log("String(valueAry)是:"+String(valueAry))
        const searchResult = await contract.evaluateTransaction(...valueAry);
        // return searchResult
        console.log(`searchResult返回结果是${searchResult.toString()}`)
        return  {
            "code":"200",
            "result":"true",
            "searchResult": searchResult.toString()
        };

       // console.log(`Transaction has been evaluated, result is: ${result.toString()}`);

    } catch (error) {
        console.error(`Failed to evaluate transaction: ${error}`);
        return {
            "code":"20011",
            "result":"false",
            "message":error.message
        }
    }
}