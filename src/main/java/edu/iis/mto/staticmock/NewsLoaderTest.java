package edu.iis.mto.staticmock;

import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import edu.iis.mto.staticmock.reader.NewsReader;
import org.powermock.api.mockito.PowerMockito;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ ConfigurationLoader.class, NewsReaderFactory.class })
public class NewsLoaderTest {

	NewsLoader newsLoader;
		IncomingNews incomingNews;
	
		ConfigurationLoader configurationLoader;
		NewsReaderFactory newsReaderFactory;
		Configuration config;
		NewsReader reader;
	
		public void Config() throws Exception {
	mockStatic(ConfigurationLoader.class);
		mockStatic(NewsReaderFactory.class);

		NewsLoader newsLoader = new NewsLoader();
		IncomingNews incomingNews = new IncomingNews();
		incomingNews.add(mockIncomingInfo(SubsciptionType.A));
		incomingNews.add(mockIncomingInfo(SubsciptionType.A));
		incomingNews.add(mockIncomingInfo(SubsciptionType.B));
		incomingNews.add(mockIncomingInfo(SubsciptionType.NONE));

		ConfigurationLoader configurationLoader = mock(ConfigurationLoader.class);
		NewsReaderFactory newsReaderFactory = mock(NewsReaderFactory.class);
		Configuration config = mock(Configuration.class);
		NewsReader reader = mock(NewsReader.class);

		when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
		when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(
				config);

		when(NewsReaderFactory.getReader(Mockito.anyString())).thenReturn(
				reader);
		when(reader.read()).thenReturn(incomingNews);

		PublishableNews publishableNews = newsLoader.loadNews();
	}



	@Test
	public void loadNews_expectedNoneSubscribentContent_and_OnePublicContent() {
		PublishableNews publishableNews = newsLoader.loadNews();
		assertThat(publishableNews.getPublicContent().size(), is(1));
		assertThat(publishableNews.getSubscribentContent().size(), is(3));
	}

	@Test
	public void behaviorTest_getReaderStarts_exptectedOne () throws Exception {
 		PublishableNews publishableNews = newsLoader.loadNews();
		PowerMockito.verifyStatic(Mockito.times(1));
		NewsReaderFactory.getReader(Mockito.anyString());
	}
	private IncomingInfo mockIncomingInfo(SubsciptionType type) {

		IncomingInfo incomingInfo = mock(IncomingInfo.class);
		when(incomingInfo.requiresSubsciption()).thenReturn(type != SubsciptionType.NONE);
		when(incomingInfo.getSubscriptionType()).thenReturn(type);

		return incomingInfo;
	}

}
