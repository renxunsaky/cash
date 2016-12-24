package com.xyz.jpos;

import jpos.JposException;
import jpos.services.CATService19;


public class CATService extends DeviceService implements CATService19 {

	@Override
	public boolean getCapStatisticsReporting() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapUpdateStatistics() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetStatistics(String arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieveStatistics(String[] arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateStatistics(String arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPaymentMedia() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPaymentMedia(int arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void accessDailyLog(int arg0, int arg1, int arg2)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizeCompletion(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizePreSales(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizeRefund(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizeSales(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizeVoid(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void authorizeVoidPreSales(int arg0, long arg1, long arg2, int arg3)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkCard(int arg0, int arg1) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearOutput() throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAccountNumber() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAdditionalSecurityInformation() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getApprovalCode() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getAsyncMode() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAdditionalSecurityInformation() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAuthorizeCompletion() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAuthorizePreSales() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAuthorizeRefund() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAuthorizeVoid() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapAuthorizeVoidPreSales() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapCenterResultCode() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapCheckCard() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCapDailyLog() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getCapInstallments() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapPaymentDetail() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getCapPowerReporting() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getCapTaxOthers() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapTrainingMode() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapTransactionNumber() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getCardCompanyID() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCenterResultCode() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDailyLog() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPaymentCondition() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPaymentDetail() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPowerNotify() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPowerState() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSequenceNumber() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSlipNumber() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getTrainingMode() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTransactionNumber() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTransactionType() throws JposException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAdditionalSecurityInformation(String arg0)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAsyncMode(boolean arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPowerNotify(int arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTrainingMode(boolean arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cashDeposit(int arg0, long arg1, int arg2) throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void compareFirmwareVersion(String arg0, int[] arg1)
			throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long getBalance() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getCapCashDeposit() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapCompareFirmwareVersion() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapLockTerminal() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapLogStatus() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapUnlockTerminal() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getCapUpdateFirmware() throws JposException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getLogStatus() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getSettledAmount() throws JposException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void lockTerminal() throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockTerminal() throws JposException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateFirmware(String arg0) throws JposException {
		// TODO Auto-generated method stub
		
	}

}
