// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { useMsal } from '@azure/msal-react';
import { Body1, Button, Text, Title3 } from '@fluentui/react-components';
import React from 'react';
import { useSharedClasses } from '../../styles';
import { getErrorDetails } from '../utils/TextUtils';

export const Login: React.FC = () => {
    const { instance } = useMsal();
    const classes = useSharedClasses();

    return (
        <div className={classes.informativeView}>
            <Title3>Login with your Microsoft Account</Title3>
            <Body1>
                {"Don't have an account? Create one for free at"}{' '}
                <a href="https://account.microsoft.com/" target="_blank" rel="noreferrer">
                    https://account.microsoft.com/
                </a>
            </Body1>

            <Button
                style={{ padding: 0 }}
                appearance="transparent"
                onClick={() => {
                    instance.loginRedirect().catch((e: unknown) => {
                        alert(`Error signing in: ${getErrorDetails(e)}`);
                    });
                }}
                data-testid="signinButton"
            >
                <Text >Sign In</Text>
            </Button>
        </div>
    );
};
