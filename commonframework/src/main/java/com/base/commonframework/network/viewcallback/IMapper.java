package com.base.commonframework.network.viewcallback;

/**
 * An interface, provides the contract for mappers, which will map the json properties with view model properties.
 */
public interface IMapper<ResponseModel, ViewModel> {
    /**
     * A method used to convert the json network model to view model used by view.
     *
     * @param responseModel responseModel model
     * @return view model used by view.
     */
    ViewModel toViewModel(ResponseModel responseModel);
}
