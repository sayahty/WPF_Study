using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.UI;

namespace celia.game
{
	public class test : MonoBehaviour
	{
        public Text t;
		public void Test()
        {
            UnityWebRequest r = UnityWebRequest.Get("https://xlycs.res.rastargame.com/NetAssets_Test/GameSetting.json");

            r.SendWebRequest();

            while (!r.isDone)
            {
                System.Threading.Thread.Sleep(1);
            }

            if (r.error == null)
            {
                string content = DownloadHandlerBuffer.GetContent(r);
                t.text = content;
            }
            else
            {
                Debug.Log("Download error:" + r.error);
            }
        }
	}
}