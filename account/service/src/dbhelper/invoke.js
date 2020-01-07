/*
 * SPDX-License-Identifier: Apache-2.0
 */

'use strict';

const { FileSystemWallet, Gateway } = require('fabric-network');
const path = require('path');

const ccpPathBackup = path.resolve(__dirname, '..', '..', 'first-network', 'connection-org1.json');
const ccpPath = path.resolve('/root/Project/fabric-samples/first-network', 'connection-org1.json');

exports.invokeMain=async (userCustom,channelCustom,contractCustom,params)=> {
    try {

        // Create a new file system based wallet for managing identities.
        const walletPath = path.join(process.cwd(), 'wallet');
        const wallet = new FileSystemWallet(walletPath);
        console.log(`Wallet path: ${walletPath}`);


        console.log("params是:"+JSON.stringify({...params.Record}))
        console.log("params是:"+{...params.Record})

        console.log("params是:"+JSON.stringify({...params}))

        // return 1;
        //1."userCustom":"user1"
        //2."channelCustom":"mychannel"
        //3."contractCustom":"fabcar"
        // Check to see if we've already enrolled the user.
        const userExists = await wallet.exists(userCustom);
        if (!userExists) {
            console.log('An identity for the user "user1" does not exist in the wallet');
            console.log('Run the registerUser.js application before retrying');
            return{
                "code":"20012",
                "result":"false",
                "message":"用户id不存在"
            };
        }

        // Create a new gateway for connecting to our peer node.
        console.log("打印到了gateway")
        const gateway = new Gateway();
        await gateway.connect(ccpPath, { wallet, identity: userCustom, discovery: { enabled: true, asLocalhost: true } });

        console.log("打印到了network")
        // Get the network (channel) our contract is deployed to.
        const network = await gateway.getNetwork(channelCustom);

        console.log("打印到了contract")
        // Get the contract from the network.
        const contract = network.getContract(contractCustom);

        // Submit the specified transaction.
        // createCar transaction - requires 5 argument, ex: ('createCar', 'CAR12', 'Honda', 'Accord', 'Black', 'Tom')
        // changeCarOwner transaction - requires 2 args , ex: ('changeCarOwner', 'CAR10', 'Dave')
        // await contract.submitTransaction('createCar', 'CAR123', 'Honda', 'Accord', 'Black', 'Tom');

        var keyAry = [];
        var valueAry = [params.operation,params.Key];
        for(var key in params.Record){
            keyAry.push(key);
            valueAry.push(params.Record[key]);
        }
        console.log("value的值是:"+valueAry)

        // return 1;
        console.log("String(valueAry)是:"+String(valueAry))
        console.log("...valueAry是:"+valueAry.join())
        await contract.submitTransaction(...valueAry);
        
        console.log('Transaction has been submitted');
        // Disconnect from the gateway.
        await gateway.disconnect();
        return {
            "code":"200",
            "result":"success",
            "userCustom":userCustom,
            "channelCustom":channelCustom,
            "contractCustom":contractCustom,
            "params":params
        }

    } catch (error) {
        console.error(`Failed to submit transaction: ${error}`);
        process.exit(1);
    }
}
