package com.freightcom.api.model.views;




import com.freightcom.api.model.DisputeInformation;
import com.freightcom.api.model.TransactionalEntity;
import com.freightcom.api.model.UserRole;


public class DisputeView extends BaseView
{
    protected final DisputeInformation dispute;
    protected final UserRole role;

    public static DisputeView get(final UserRole role, final DisputeInformation dispute)
    {
        return new DisputeView(role, dispute);
    }

    protected DisputeView(final UserRole role, final DisputeInformation dispute)
    {
        this.dispute = dispute;
        this.role = role;
    }

    @Override
    public TransactionalEntity object()
    {
        return dispute;
    }

    public DisputeInformation.Status getStatus()
    {
        return dispute.getStatus();
    }

}
