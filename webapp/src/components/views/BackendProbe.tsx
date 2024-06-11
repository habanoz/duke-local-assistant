// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { Body1, Spinner, Title3 } from '@fluentui/react-components';
import { FC, useEffect } from 'react';
import { BackendServiceUrl } from '../../libs/services/BaseService';
import { useAppDispatch } from '../../redux/app/hooks';
import { useSharedClasses } from '../../styles';

interface IData {
    onBackendFound: () => void;
}

export const BackendProbe: FC<IData> = ({ onBackendFound }) => {
    const classes = useSharedClasses();
    const dispatch = useAppDispatch();

    useEffect(() => {
        onBackendFound();
    }, [dispatch, onBackendFound]);

    return (
        <>
            <div className={classes.informativeView}>
                    <Title3>Connecting...</Title3>
                    <Spinner />
                    <Body1>
                        This app expects to find a server running at <strong>{BackendServiceUrl}</strong>
                    </Body1>
                    <Body1>
                        To run the server locally, use Visual Studio, Visual Studio Code, or type the following command:{' '}
                        <code>
                            <strong>dotnet run</strong>
                        </code>
                    </Body1>
                    <Body1>
                        If running locally, ensure that you have the{' '}
                        <code>
                            <b>REACT_APP_BACKEND_URI</b>
                        </code>{' '}
                        variable set in your <b>webapp/.env</b> file
                    </Body1>
                </div>
        </>
    );
};
