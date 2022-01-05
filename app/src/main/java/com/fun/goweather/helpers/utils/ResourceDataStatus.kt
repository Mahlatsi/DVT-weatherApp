package com.`fun`.goweather.helpers.utils

class ResourceDataStatus<T> {
    var state: DataState
    var data: T ?=null
    lateinit var error: String

    constructor(success: DataState, data: T) {
        this.state = success
        this.data = data
        this.data = data
    }

    constructor(error: String) {
        this.state = DataState.ERROR
        this.error = error
    }

    constructor() {
        this.state = DataState.LOADING
    }

    companion object {
        fun <T> postSuccess(data: T): ResourceDataStatus<T> {
            return ResourceDataStatus(
                DataState.SUCCESS,
                data
            )
        }

        fun <T> postError(throwable: String): ResourceDataStatus<T> {
            return ResourceDataStatus(throwable)
        }

        fun <T> postLoading(): ResourceDataStatus<T> {
            return ResourceDataStatus()
        }
    }
}