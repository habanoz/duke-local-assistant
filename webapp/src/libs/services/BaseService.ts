// Copyright (c) Microsoft. All rights reserved.
// Copyright (c) huseyinabanox@gmail.com

import { URLSearchParams } from 'url';
import { ChatStreamEvent } from '../hooks/useChat';
import { IAsk } from '../semantic-kernel/model/Ask';
interface ServiceRequest {
    commandPath: string;
    method?: string;
    body?: unknown;
    query?: URLSearchParams;
}

const noResponseBodyStatusCodes = [202, 204];

export const BackendServiceUrl =
    process.env.REACT_APP_BACKEND_URI == null || process.env.REACT_APP_BACKEND_URI.trim() === ''
        ? window.origin
        : process.env.REACT_APP_BACKEND_URI;
export const NetworkErrorMessage = '\n\nPlease check that your backend is running and that it is accessible by the app';

export class BaseService {
    constructor(protected readonly serviceUrl: string = BackendServiceUrl) {}

    protected readonly getResponseAsync = async <T>(
        request: ServiceRequest,
    ): Promise<T> => {
        const { commandPath, method, body, query } = request;

        const isFormData = body instanceof FormData;

        const headers = new Headers(
                    undefined,
        );

        if (!isFormData) {
            headers.append('Content-Type', 'application/json');
        }

        try {
            const requestUrl = new URL(commandPath, this.serviceUrl);
            if (query) {
                requestUrl.search = `?${query.toString()}`;
            }

            const response = await fetch(requestUrl, {
                method: method ?? 'GET',
                body: isFormData ? body : JSON.stringify(body),
                headers,
            });

            if (!response.ok) {
                if (response.status === 504) {
                    throw Object.assign(new Error('The request timed out. Please try sending your message again.'));
                }

                const responseText = await response.text();
                const responseDetails = responseText.split('--->');
                const errorDetails =
                    responseDetails.length > 1
                        ? `${responseDetails[0].trim()} ---> ${responseDetails[1].trim()}`
                        : responseDetails[0];
                const errorMessage = `${response.status}: ${response.statusText}${errorDetails}`;

                throw Object.assign(new Error(errorMessage));
            }

            return (noResponseBodyStatusCodes.includes(response.status) ? {} : await response.json()) as T;
        } catch (e: any) {
            let isNetworkError = false;
            if (e instanceof TypeError) {
                // fetch() will reject with a TypeError when a network error is encountered.
                isNetworkError = true;
            }
            throw Object.assign(new Error(`${e as string} ${isNetworkError ? NetworkErrorMessage : ''}`));
        }
    };

    protected readonly handleResponseEventStream2 = async (
        request: ServiceRequest,
        event_handler: (event: ChatStreamEvent) => void,
    ) => {
        const { commandPath, method, body, query } = request;

        const isFormData = body instanceof FormData;
        const ask = body as IAsk;
        const message = { role: 'user', content: ask.input };
        const chatMessages = [message];

        const headers = new Headers(undefined);

        if (!isFormData) {
            headers.append('Content-Type', 'application/json');
        }

        try {
            const requestUrl = new URL(commandPath, this.serviceUrl);
            if (query) {
                requestUrl.search = `?${query.toString()}`;
            }

            const body = JSON.stringify(chatMessages);
            console.log('EventRequest:', requestUrl, body);

            const response = await fetch(requestUrl, {
                method: method ?? 'GET',
                body: body,
                headers,
            });

            if (!response.ok) {
                throw new Error(`Failed to generate text: ${await response.text()}`);
            }

            const encoder = new TextDecoderStream();
            const reader = response.body?.pipeThrough(encoder).getReader();

            // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
            while (true) {
                // read the stream and log the outputs to console
                const out = (await reader?.read()) ?? { done: false, value: undefined };
                // we read, if it's done we cancel
                if (out.done) {
                    //console.log('Chuk Out Done');
                    await reader?.cancel();
                    return;
                }

                if (!out.value) {
                    return;
                }

                let raw_output_value = null;
                try {
                    raw_output_value = out.value;
                    console.log('Chuk Data(raw):', raw_output_value);
                } catch (e) {
                    console.log('Chuk Data(raw) Error:', e);
                    return;
                }

                const events = raw_output_value.split('\n').filter((line) => line !== '');

                // Iterate over the resulting array of event strings and parse each one using JSON.parse()
                events.forEach((eventString) => {
                    if (eventString.startsWith('data:')) {
                        const eventData = eventString.slice(5); // Remove the "data:" prefix
                        try {
                            if (eventData.length > 0) {
                                // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
                                const event = JSON.parse(eventData) as ChatStreamEvent;
                                //console.log('Chuk Data (json):', event);
                                event_handler(event);
                            }
                        } catch (error) {
                            console.error('Error parsing event data:', error);
                        }
                    }
                });
            }
        } catch (e: any) {
            let isNetworkError = false;
            if (e instanceof TypeError) {
                // fetch() will reject with a TypeError when a network error is encountered.
                isNetworkError = true;
            }
            throw Object.assign(new Error(`${e as string} ${isNetworkError ? NetworkErrorMessage : ''}`));
        }
    };

    protected readonly handleResponseEventStream = async (
        request: ServiceRequest,
        event_handler: (event: ChatStreamEvent) => void,
    ) => {
        const { commandPath, method, body, query } = request;

        const headers = new Headers(undefined);

        headers.append('Content-Type', 'application/json');

        function handle(eventDataString: string) {
            try {
                if (!eventDataString.startsWith('data:')) {
                    console.log('Discard text:', eventDataString);
                    return;
                }
                eventDataString = eventDataString.substring(5);
                if (eventDataString.length > 0) {
                    // eslint-disable-next-line @typescript-eslint/no-unsafe-assignment
                    const event = JSON.parse(eventDataString) as ChatStreamEvent;
                    //console.log('Chunk Data (json):', event);
                    event_handler(event);
                }
            } catch (error) {
                console.error('Error parsing event data:', error);
            }
        }

        try {
            const requestUrl = new URL(commandPath, this.serviceUrl);
            if (query) {
                requestUrl.search = `?${query.toString()}`;
            }

            const bodyJson = JSON.stringify(body);

            const response = await fetch(requestUrl, {
                method: method ?? 'GET',
                body: bodyJson,
                headers,
            });

            if (!response.ok) {
                throw new Error(`Failed to generate text: ${await response.text()}`);
            } else {
                console.log('Response is OK', new Date());
            }

            const encoder = new TextDecoderStream();
            const reader = response.body?.pipeThrough(encoder).getReader();

            let textBuffer = '';
            // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
            while (true) {
                // read the stream and log the outputs to console
                const out = (await reader?.read()) ?? { done: false, value: undefined };
                // we read, if it's done we cancel
                if (out.done) {
                    //console.log('Chuk Out Done');
                    await reader?.cancel();
                    handle(textBuffer);
                    return;
                }

                if (!out.value) {
                    return;
                }

                textBuffer += out.value;
                console.log('Received Text:', new Date(), out.value);

                if (!textBuffer.startsWith('data:')) continue;

                const second_data_start = textBuffer.substring(1).indexOf('data:') + 1; // exclude first data: then add 1 for correct index.
                if (second_data_start == 0) continue;

                const eventData = textBuffer.substring(0, second_data_start);
                textBuffer = textBuffer.substring(second_data_start);

                handle(eventData);
            }
        } catch (e: any) {
            let isNetworkError = false;
            if (e instanceof TypeError) {
                // fetch() will reject with a TypeError when a network error is encountered.
                isNetworkError = true;
            }
            throw Object.assign(new Error(`${e as string} ${isNetworkError ? NetworkErrorMessage : ''}`));
        }
    };
}
