package com.github.rssreader.base.data.entity.mapper

import io.reactivex.Observable


interface Mapper<T, S> {

    fun transformToEntity(model: T) : Observable<S>

    fun transformToDomainModel(entity: S) : Observable<T>

}