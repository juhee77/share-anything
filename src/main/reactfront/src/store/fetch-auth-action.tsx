import axios, {AxiosError, AxiosResponse} from 'axios';

type ServerError = { errorMessage: string };
type LoginFailType = { status: number, error: string,};

interface FetchData {
  method: string,
  url: string,
  data? : {},
  header : {},
}

const fetchAuth = async (fetchData: FetchData) => {
  const method = fetchData.method;
  const url = fetchData.url;
  const data = fetchData.data;
  const header = fetchData.header;
  
  try {
    const response:AxiosResponse<any, any> | false =
    (method === 'get' && (await axios.get(url, header))) ||
    (method === 'post' && (await axios.post(url, data, header))) ||
    (method === 'put' && (await axios.put(url, data, header))) ||
    (method === 'delete' && (await axios.delete(url, header))
    );
    
    if(response && response.data.error) {
      console.log((response.data as LoginFailType).error);
      alert("Wrong ID or Password");
      return null;
    }

    if (!response) {
      alert("FAIL");
      return null;
    }

    return response;

  } catch(err) {
    
    if (axios.isAxiosError(err)) {
      const serverError = err as AxiosError<ServerError>;
      if (serverError && serverError.response) {
        console.log(serverError.response.data);
        alert("FAIL");
        return null;
      }
    }

    console.log(err);
    alert("FAIL");
    return null;
  }
  
}

const GET = ( url:string, header:{} ) => {
  return fetchAuth({method: 'get', url, header});
};

const POST = ( url:string, data: {}, header:{}) => {
  return fetchAuth({method: 'post', url, data, header});
};

const PUT = async ( url:string, data: {}, header:{}) => {
  return fetchAuth({method: 'put', url, data, header});
};

const DELETE = async ( url:string, header:{} ) => {
  return fetchAuth({method: 'delete', url, header});
};

export { GET, POST, PUT, DELETE }