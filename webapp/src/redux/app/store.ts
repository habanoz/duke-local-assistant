// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import {
    Action,
    Dispatch,
    MiddlewareAPI,
    ThunkMiddleware,
    Tuple,
    UnknownAction,
    configureStore,
} from '@reduxjs/toolkit';
import { AppState } from '../features/app/AppState';
import { ConversationsState } from '../features/conversations/ConversationsState';
import { UsersState } from '../features/users/UsersState';
import resetStateReducer, { resetApp } from './rootReducer';

export type StoreMiddlewareAPI = MiddlewareAPI<Dispatch, RootState>;
export type Store = typeof store;
export const store = configureStore<RootState, Action, Tuple<Array<ThunkMiddleware<RootState, UnknownAction>>>>({
    reducer: resetStateReducer,
    middleware: (getDefaultMiddleware) => getDefaultMiddleware(),
});

export interface RootState {
    app: AppState;
    conversations: ConversationsState;
    users: UsersState;
}

export const getSelectedChatID = (): string => {
    return store.getState().conversations.selectedId;
};

export type AppDispatch = typeof store.dispatch;

// Function to reset the app state
export const resetState = () => {
    store.dispatch(resetApp());
};
